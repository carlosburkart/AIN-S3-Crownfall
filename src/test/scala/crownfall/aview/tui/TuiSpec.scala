package crownfall.aview.tui

import crownfall.controller.*
import crownfall.model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TuiSpec extends AnyWordSpec with Matchers:

  "A Tui" should {

    "return false for 'quit' command" in {
      val troop = Troop.create(1, Side.One, TroopKind.Knight)
      val start = Position(0, 1)
      val board = Board(8, 8, Map(start -> troop))
      val controller = Controller(board)
      val tui = Tui(controller)
      val input = "quit"
      tui.processInput(input) should be(false)
    }

    "move a troop and keep running after a valid move command" in {
      val troop = Troop.create(1, Side.One, TroopKind.Knight)
      val start = Position(0, 1)
      val target = Position(0, 2)
      val board = Board(8, 8, Map(start -> troop))
      val controller = Controller(board)
      val tui = Tui(controller)
      val input = "move A2 A3"
      tui.processInput(input) should be(true)
      controller.board.troopAt(target) should be(Some(troop))
    }

    "not move a troop but keep running after an invalid move command" in {
      val troop = Troop.create(1, Side.One, TroopKind.Knight)
      val pos = Position(0, 1)
      val board = Board(8, 8, Map(pos -> troop))
      val controller = Controller(board)
      val tui = Tui(controller)
      val input = "move A2 A9"
      tui.processInput(input) should be(true)
      controller.board should be(board)
    }

    "keep running after the commands 'info', 'help' or if the command is unknown" in {
      val troop = Troop.create(1, Side.One, TroopKind.Knight)
      val pos = Position(0, 1)
      val board = Board(8, 8, Map(pos -> troop))
      val controller = Controller(board)
      val tui = Tui(controller)
      for input <- List("info A2", "info A5", "help", "unknown") do
        tui.processInput(input) should be(true)
    }
  }