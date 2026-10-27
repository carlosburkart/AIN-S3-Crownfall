package crownfall.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class BoardSpec extends AnyWordSpec with Matchers:

  "A Board" should {

    "recognize positions inside its bounds" in {
      val board = Board(8, 8, Map())
      for pos <- List(Position(0, 0), Position(7, 7), Position(0, 7), Position(7, 0)) do
        board.isInside(pos) should be(true)
    }

    "recognize positions outside its bound" in {
      val board = Board(8, 8, Map())
      for pos <- List(Position(-1, 0), Position(0, -1), Position(8, 0), Position(0, 8)) do
        board.isInside(pos) should be(false)
    }

    "return no troop for an empty field" in {
      val troop = Troop(1, Side.One, TroopKind.Knight, 10)
      val occupied = Position(3, 5)
      val empty = Position(2, 4)
      val board = Board(8, 8, Map(occupied -> troop))
      board.troopAt(empty) should be(None)
    }

    "return the troop standing on a field" in {
      val troop = Troop(1, Side.One, TroopKind.Knight, 10)
      val pos = Position(3, 5)
      val board = Board(8, 8, Map(pos -> troop))
      board.troopAt(pos) should be(Some(troop))
    }

    "not be created without columns or rows" in {
      an[IllegalArgumentException] should be thrownBy {
        Board(0, 8, Map())
      }
      an[IllegalArgumentException] should be thrownBy {
        Board(8, 0, Map())
      }
    }

    "not be created with troops outside its bounds" in {
      val troop = Troop(1, Side.One, TroopKind.Knight, 10)
      val outside = Position(20, 20)
      an[IllegalArgumentException] should be thrownBy {
        Board(8, 8, Map(outside -> troop))
      }
    }
  }