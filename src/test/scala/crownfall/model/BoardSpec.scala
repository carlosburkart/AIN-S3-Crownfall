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

    "move a troop to a free field" in {
      val posStart = Position(3, 5)
      val posEnd = Position(3, 6)
      val troop = Troop.create(1, Side.One, TroopKind.Archer)
      val board = Board(8, 8, Map(posStart -> troop))
      val result = board.move(posStart, posEnd)
      val expectedBoard = Board(8, 8, Map(posEnd -> troop))
      result should be(Right(expectedBoard))
    }

    "not move a troop from an empty field" in {
      val occupied = Position(3, 5)
      val empty = Position(2, 3)
      val target = Position(3, 3)
      val troop = Troop.create(1, Side.One, TroopKind.Archer)
      val board = Board(8, 8, Map(occupied -> troop))
      val result = board.move(empty, target)
      result.isLeft should be(true)
    }

    "not move a troop outside the board" in {
      val start = Position(0, 0)
      val target = Position(-1, 0)
      val troop = Troop.create(1, Side.One, TroopKind.Archer)
      val board = Board(8, 8, Map(start -> troop))
      val result = board.move(start, target)
      result.isLeft should be(true)
    }

    "not move a troop on an occupied field" in {
      val posMover = Position(3, 5)
      val posBlocker = Position(4, 5)
      val mover = Troop.create(1, Side.One, TroopKind.Knight)
      val blocker = Troop.create(2, Side.One, TroopKind.Knight)
      val board = Board(8, 8, Map(posMover -> mover, posBlocker -> blocker))
      val result = board.move(posMover, posBlocker)
      result.isLeft should be(true)
    }

    "not move a troop further than its movement" in {
      val start = Position(3, 5)
      val target = Position(5, 5)
      val troop = Troop.create(1, Side.One, TroopKind.Mage)
      val board = Board(8, 8, Map(start -> troop))
      val result = board.move(start, target)
      result.isLeft should be(true)
    }
  }

  "The initial board" should {

    "have troops on both sides" in {
      val board = Board.initial
      val countOne = board.troops.values.count(troop => troop.owner == Side.One)
      val countTwo = board.troops.values.count(troop => troop.owner == Side.Two)
      countOne should be > 0
      countTwo should be > 0
    }

    "be mirrored between both sides" in {
      val board = Board.initial
      for (pos, troop) <- board.troops do
        val mirroredRow = board.rows - (1 + pos.row)
        val mirroredPos = Position(pos.col, mirroredRow)
        val mirrored = board.troopAt(mirroredPos)
        mirrored.map(other => other.kind) should be(Some(troop.kind))
        mirrored.map(other => other.owner) should be(Some(troop.owner.opponent))
    }

    "give every troop a unique id" in {
      val board = Board.initial
      val ids = board.troops.values.map(troop => troop.id)
      ids.size should be(ids.toSet.size)
    }
  }