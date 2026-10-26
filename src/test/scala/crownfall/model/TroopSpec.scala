package crownfall.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class TroopSpec extends AnyWordSpec with Matchers:

  "A Troop" should {

    "lose exactly the damage taken from its hp" in {
      val troop = Troop(1, Side.One, TroopKind.Knight, 10)
      val damaged = troop.takeDamage(3)
      damaged.hp should be(7)
    }

    "stay unchanged after taking damage" in {
      val troop = Troop(1, Side.One, TroopKind.Knight, 10)
      val damaged = troop.takeDamage(3)
      val expected = Troop(1, Side.One, TroopKind.Knight, 10)
      troop should be(expected)
    }

    "be defeated when its hp is 0" in {
      val troop = Troop(1, Side.One, TroopKind.Knight, 0)
      troop.isDefeated should be(true)
    }

    "not be defeated when it has hp left" in {
      val troop = Troop(1, Side.One, TroopKind.Knight, 1)
      troop.isDefeated should be(false)
    }

    "not drop below 0 hp" in {
      val troop = Troop(1, Side.One, TroopKind.Knight, 3)
      val damaged = troop.takeDamage(7)
      damaged.hp should be(0)
    }

    "not be created with negative hp" in {
      an[IllegalArgumentException] should be thrownBy {
        Troop(1, Side.One, TroopKind.Knight, -1)
      }
    }

    "not have more hp than its max hp" in {
      an[IllegalArgumentException] should be thrownBy {
        Troop(1, Side.One, TroopKind.Knight, TroopKind.Knight.maxHp + 1)
      }
    }
    
    "not take negative damage" in {
      val troop = Troop(1, Side.One, TroopKind.Knight, 5)
      an[IllegalArgumentException] should be thrownBy {
        troop.takeDamage(-1)
      }
    }
  }