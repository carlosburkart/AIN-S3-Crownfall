package crownfall.aview.tui

import crownfall.controller.*
import crownfall.model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TuiSpec extends AnyWordSpec with Matchers:

  private val enemyPos = Position(7, 7)
  private val enemy = Troop.create(99, Side.Two, TroopKind.Knight)

  "A Tui" should {

    "return false for 'quit' command" in {
      val troop = Troop.create(1, Side.One, TroopKind.Knight)
      val start = Position(0, 1)
      val board = Board(8, 8, Map(start -> troop, enemyPos -> enemy))
      val controller = Controller(board)
      val tui = Tui(controller)
      val input = "quit"
      tui.processInput(input) should be(false)
    }

    "move a troop and keep running after a valid move command" in {
      val troop = Troop.create(1, Side.One, TroopKind.Knight)
      val start = Position(0, 1)
      val target = Position(0, 2)
      val board = Board(8, 8, Map(start -> troop, enemyPos -> enemy))
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

    "attack a troop and keep running after a valid attack command" in {
      val archer = Troop.create(1, Side.One, TroopKind.Archer)
      val target = Troop.create(2, Side.Two, TroopKind.Knight)
      val board = Board(8, 8, Map(Position(0, 1) -> archer, Position(0, 3) -> target, enemyPos -> enemy))
      val controller = Controller(board)
      val tui = Tui(controller)
      tui.processInput("attack A2 A4") should be(true)
      controller.board.troopAt(Position(0, 3)).map(t => t.hp) should be(Some(7))
    }

    "not attack but keep running after an invalid attack command" in {
      val archer = Troop.create(1, Side.One, TroopKind.Archer)
      val board = Board(8, 8, Map(Position(0, 1) -> archer, enemyPos -> enemy))
      val controller = Controller(board)
      val tui = Tui(controller)
      tui.processInput("attack A2 A4") should be(true)
      controller.board should be(board)
    }

    "keep running after the game is over, so the last action can be undone" in {
      val archer = Troop.create(1, Side.One, TroopKind.Archer)
      val lastEnemy = Troop.create(2, Side.Two, TroopKind.Mage) // 4 hp, 6 * 5 / 6 = 5 damage
      val board = Board(8, 8, Map(Position(0, 1) -> archer, Position(0, 3) -> lastEnemy))
      val controller = Controller(board)
      val tui = Tui(controller)
      tui.processInput("attack A2 A4") should be(true)
      tui.processInput("undo") should be(true)
      controller.board should be(board)
    }

    "undo and redo the last action" in {
      val troop = Troop.create(1, Side.One, TroopKind.Knight)
      val board = Board(8, 8, Map(Position(0, 1) -> troop, enemyPos -> enemy))
      val controller = Controller(board)
      val tui = Tui(controller)
      tui.processInput("move A2 A3")
      tui.processInput("undo") should be(true)
      controller.board should be(board)
      tui.processInput("redo") should be(true)
      controller.board.troopAt(Position(0, 2)) should be(Some(troop))
    }

    "keep running if there is nothing to undo or redo" in {
      val tui = Tui(Controller(Board(8, 8, Map(enemyPos -> enemy))))
      tui.processInput("undo") should be(true)
      tui.processInput("redo") should be(true)
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