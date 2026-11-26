package crownfall.controller

import crownfall.model.*
import crownfall.util.CountingObserver
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ControllerAttackSpec extends AnyWordSpec with Matchers:

  private class FixedDamage(amount: Int) extends DamageStrategy:
    def damage(attacker: Troop, defender: Troop): Int = amount

  private val archerPos = Position(0, 1)
  private val enemyPos  = Position(0, 3) // distance 2, archer range 3
  private val otherPos  = Position(7, 7)

  private val archer = Troop.create(1, Side.One, TroopKind.Archer)
  private val enemy  = Troop.create(2, Side.Two, TroopKind.Knight) // 10 hp, defense 5
  private val other  = Troop.create(3, Side.Two, TroopKind.Knight)

  private def board = Board(8, 8, Map(archerPos -> archer, enemyPos -> enemy, otherPos -> other))

  "A Controller" should {

    "attack with the strategy it was given" in {
      val controller = Controller(board, FixedDamage(2))
      controller.attack(archerPos, enemyPos)
      controller.board.troopAt(enemyPos).map(t => t.hp) should be(Some(8))
    }

    "use RatioDamage if no strategy is given" in {
      val controller = Controller(board)
      controller.attack(archerPos, enemyPos)
      controller.board.troopAt(enemyPos).map(t => t.hp) should be(Some(7)) // 6 * 5 / 10 = 3
    }

    "give the turn to the opponent after a valid attack" in {
      val controller = Controller(board, FixedDamage(1))
      controller.attack(archerPos, enemyPos)
      controller.state should be(Turn(Side.Two))
    }

    "not attack with a troop of the side that is not on turn" in {
      val controller = Controller(board, FixedDamage(1))
      val result = controller.attack(enemyPos, archerPos)
      result should be(Err(GameError.NotYourTroop))
      controller.board should be(board)
    }

    "keep the board and the turn after an invalid attack" in {
      val controller = Controller(board, FixedDamage(1))
      val result = controller.attack(archerPos, Position(5, 5))
      result should be(Err(GameError.NoTarget))
      controller.board should be(board)
      controller.state should be(Turn(Side.One))
    }

    "end the game when the last enemy troop is defeated" in {
      val lastEnemy = Board(8, 8, Map(archerPos -> archer, enemyPos -> enemy))
      val controller = Controller(lastEnemy, FixedDamage(100))
      controller.attack(archerPos, enemyPos)
      controller.state should be(GameOver(Side.One))
    }

    "notify observers after a valid attack" in {
      val observer = CountingObserver()
      val controller = Controller(board, FixedDamage(1))
      controller.add(observer)
      controller.attack(archerPos, enemyPos)
      observer.count should be(1)
    }

    "not notify observers after an invalid attack" in {
      val observer = CountingObserver()
      val controller = Controller(board, FixedDamage(1))
      controller.add(observer)
      controller.attack(archerPos, Position(5, 5))
      observer.count should be(0)
    }
  }
