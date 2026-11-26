package crownfall.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BoardAttackSpec extends AnyWordSpec with Matchers:

  // A strategy only for tests: always deals the same damage.
  // This way the tests check attack() itself, independent of any damage formula.
  private class FixedDamage(amount: Int) extends DamageStrategy:
    def damage(attacker: Troop, defender: Troop): Int = amount

  private val attackerPos = Position(3, 3)
  private val archer = Troop.create(1, Side.One, TroopKind.Archer) // range 3
  private val enemy  = Troop.create(2, Side.Two, TroopKind.Knight) // 10 hp

  "A Board" should {

    "damage an enemy troop in range" in {
      val targetPos = Position(3, 5)
      val board = Board(8, 8, Map(attackerPos -> archer, targetPos -> enemy))
      val result = board.attack(attackerPos, targetPos, FixedDamage(3))
      val expected = Board(8, 8, Map(attackerPos -> archer, targetPos -> enemy.copy(hp = 7)))
      result should be(Right(expected))
    }

    "remove a troop that is defeated by the attack" in {
      val targetPos = Position(3, 5)
      val board = Board(8, 8, Map(attackerPos -> archer, targetPos -> enemy))
      val result = board.attack(attackerPos, targetPos, FixedDamage(100))
      val expected = Board(8, 8, Map(attackerPos -> archer))
      result should be(Right(expected))
    }

    "allow an attack at exactly the range of the attacker" in {
      val targetPos = Position(3, 6) // distance 3
      val board = Board(8, 8, Map(attackerPos -> archer, targetPos -> enemy))
      val result = board.attack(attackerPos, targetPos, FixedDamage(1))
      result.isRight should be(true)
    }

    "not attack a target further away than the range" in {
      val targetPos = Position(3, 7) // distance 4
      val board = Board(8, 8, Map(attackerPos -> archer, targetPos -> enemy))
      val result = board.attack(attackerPos, targetPos, FixedDamage(1))
      result should be(Left(GameError.OutOfRange))
    }

    "not attack from an empty field" in {
      val targetPos = Position(3, 5)
      val board = Board(8, 8, Map(targetPos -> enemy))
      val result = board.attack(attackerPos, targetPos, FixedDamage(1))
      result should be(Left(GameError.NoTroopThere))
    }

    "not attack an empty field" in {
      val board = Board(8, 8, Map(attackerPos -> archer))
      val result = board.attack(attackerPos, Position(3, 5), FixedDamage(1))
      result should be(Left(GameError.NoTarget))
    }

    "not attack a troop of the same side" in {
      val targetPos = Position(3, 4)
      val friend = Troop.create(2, Side.One, TroopKind.Knight)
      val board = Board(8, 8, Map(attackerPos -> archer, targetPos -> friend))
      val result = board.attack(attackerPos, targetPos, FixedDamage(1))
      result should be(Left(GameError.OwnTroop))
    }

    "cancel the attack if the strategy returns negative damage" in {
      val targetPos = Position(3, 5)
      val board = Board(8, 8, Map(attackerPos -> archer, targetPos -> enemy))
      val result = board.attack(attackerPos, targetPos, FixedDamage(-1))
      result should be(Left(GameError.InvalidDamage))
    }

    "cancel the attack if the strategy fails" in {
      val failing = new DamageStrategy:
        def damage(attacker: Troop, defender: Troop): Int = throw IllegalStateException("broken strategy")
      val targetPos = Position(3, 5)
      val board = Board(8, 8, Map(attackerPos -> archer, targetPos -> enemy))
      val result = board.attack(attackerPos, targetPos, failing)
      result should be(Left(GameError.InvalidDamage))
    }
  }