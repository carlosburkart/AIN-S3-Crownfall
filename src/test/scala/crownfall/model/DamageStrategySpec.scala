package crownfall.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DamageStrategySpec extends AnyWordSpec with Matchers:

  private val knight = Troop.create(1, Side.One, TroopKind.Knight) // ATK 4, DEF 5
  private val archer = Troop.create(2, Side.One, TroopKind.Archer) // ATK 6, DEF 2
  private val mage   = Troop.create(3, Side.One, TroopKind.Mage)   // ATK 8, DEF 1

  "TrueDamage" should {

    "deal the full attack of the attacker" in {
      TrueDamage.damage(knight, mage) should be(4)
      TrueDamage.damage(archer, mage) should be(6)
      TrueDamage.damage(mage, mage) should be(8)
    }

    "ignore the defense of the defender" in {
      TrueDamage.damage(mage, knight) should be(TrueDamage.damage(mage, mage)) // DEF 5 vs DEF 1
    }
  }

  "RatioDamage" should {

    // damage = attack * 5 / (defense + 5), as a whole number (integer division)

    "reduce the attack depending on the defense" in {
      RatioDamage.damage(knight, knight) should be(2) // 4 * 5 / 10
      RatioDamage.damage(archer, mage) should be(5)   // 6 * 5 / 6
      RatioDamage.damage(mage, knight) should be(4)   // 8 * 5 / 10
    }

    "round the result down" in {
      RatioDamage.damage(knight, archer) should be(2) // 20 / 7 = 2.86
      RatioDamage.damage(mage, mage) should be(6)     // 40 / 6 = 6.67
    }

    "deal less damage to a target with a higher defense" in {
      RatioDamage.damage(mage, knight) should be < RatioDamage.damage(mage, mage)
    }
  }