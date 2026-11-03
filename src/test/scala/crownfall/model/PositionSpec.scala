package crownfall.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PositionSpec extends AnyWordSpec with Matchers:

  "A Position" should {

    "be equal to another position with the same values" in {
      val pos1 = Position(2, 3)
      val pos2 = Position(2, 3)
      pos1 should be(pos2)
    }

    "have a distance of 0 to itself" in {
      val pos = Position(2, 3)
      val dis = pos.distanceTo(pos)
      dis should be(0)
    }

    "use the larger of the column and row difference as distance" in {
      val pos1 = Position(0, 3)
      val pos2 = Position(3, 5)
      val pos3 = Position(1, 7)
      val dis12 = pos1.distanceTo(pos2)
      val dis13 = pos1.distanceTo(pos3)
      dis12 should be(3)
      dis13 should be(4)
    }

    "have the same distance in both directions" in {
      val posA = Position(2, 3)
      val posB = Position(4, 6)
      val disAB = posA.distanceTo(posB)
      val disBA = posB.distanceTo(posA)
      disAB should be(disBA)
    }

    "have exactly 8 neighbors" in {
      val pos = Position(1, 1)
      val neigh = pos.neighbors
      neigh.size should be(8)
    }

    "not be one of its own neighbors" in {
      val pos = Position(1, 1)
      val neigh = pos.neighbors
      neigh should not contain pos
    }

    "have exactly the expected neighbors" in {
      val pos = Position(1, 1)
      val neigh = pos.neighbors
      val neighSet = Set(
        Position(0, 0), Position(0, 1), Position(0, 2),
        Position(1, 0),                 Position(1, 2),
        Position(2, 0), Position(2, 1), Position(2, 2)
      )
      neigh.toSet should be(neighSet)
    }
  }