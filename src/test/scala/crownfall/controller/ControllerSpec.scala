package crownfall.controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import crownfall.model.*
import crownfall.util.CountingObserver

class ControllerSpec extends AnyWordSpec with Matchers:

  "A Controller" should {

    "start with the given board" in {
      val board = Board.initial
      val controller = Controller(board)
      controller.board should be(board)
    }

    "update the board after a valid move" in {
      val troop = Troop.create(1, Side.One, TroopKind.Knight)
      val start = Position(0, 1)
      val target = Position(0, 2)
      val board = Board(8, 8, Map(start -> troop))
      val controller = Controller(board)
      controller.move(start, target)
      controller.board.troopAt(target) should be(Some(troop))
    }

    "keep the board after an invalid move" in {
      val troop = Troop.create(1, Side.One, TroopKind.Knight)
      val start = Position(0, 1)
      val target = Position(-1, 1)
      val board = Board(8, 8, Map(start -> troop))
      val controller = Controller(board)
      controller.move(start, target)
      controller.board should be(board)
    }

    "return an error for an invalid move" in {
      val troop = Troop.create(1, Side.One, TroopKind.Knight)
      val start = Position(0, 1)
      val target = Position(-1, 1)
      val board = Board(8, 8, Map(start -> troop))
      val controller = Controller(board)
      val result = controller.move(start, target)
      result.isLeft should be(true)
    }

    "notify observers after a valid move" in {
      val observer = CountingObserver()
      val troop = Troop.create(1, Side.One, TroopKind.Knight)
      val start = Position(0, 1)
      val target = Position(0, 2)
      val board = Board(8, 8, Map(start -> troop))
      val controller = Controller(board)
      controller.add(observer)
      controller.move(start, target)
      observer.count should be(1)
    }

    "not notify observers after an invalid move" in {
      val observer = CountingObserver()
      val troop = Troop.create(1, Side.One, TroopKind.Knight)
      val start = Position(0, 1)
      val target = Position(-1, 1)
      val board = Board(8, 8, Map(start -> troop))
      val controller = Controller(board)
      controller.add(observer)
      controller.move(start, target)
      observer.count should be(0)
    }
  }