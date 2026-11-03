package crownfall.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class TroopKindSpec extends AnyWordSpec with Matchers:

  "Every TroopKind" should {

    "have positive maxHp" in {
      for kind <- TroopKind.values do
        kind.maxHp should be > 0
    }

    "not have negative attack" in {
      for kind <- TroopKind.values do
        kind.attack should be >= 0
    }

    "not have negative defense" in {
      for kind <- TroopKind.values do
        kind.defense should be >= 0
    }

    "have a range of at least 1" in {
      for kind <- TroopKind.values do
        kind.range should be >= 1
    }

    "have a movement of at least 1" in {
      for kind <- TroopKind.values do
        kind.movement should be >= 1
    }
  }
