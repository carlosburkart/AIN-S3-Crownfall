package crownfall.controller

import crownfall.model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ActionCommandSpec extends AnyWordSpec with Matchers:

  private val knight = Troop.create(1, Side.One, TroopKind.Knight)
  private val boardBefore = Board(8, 8, Map(Position(0, 0) -> knight))
  private val boardAfter  = Board(8, 8, Map(Position(0, 1) -> knight))

  private val beforeSnapshot = Snapshot(boardBefore, Turn(Side.One))
  private val afterSnapshot  = Snapshot(boardAfter, Turn(Side.Two))

  "An ActionCommand" should {

    "set the situation after the action when it is executed" in {
      val controller = Controller(boardBefore)
      ActionCommand(controller, beforeSnapshot, afterSnapshot).doStep()
      controller.snapshot should be(afterSnapshot)
    }

    "set the situation before the action when it is undone" in {
      val controller = Controller(boardBefore)
      val command = ActionCommand(controller, beforeSnapshot, afterSnapshot)
      command.doStep()
      command.undoStep()
      controller.snapshot should be(beforeSnapshot)
    }

    "set the situation after the action again when it is redone" in {
      val controller = Controller(boardBefore)
      val command = ActionCommand(controller, beforeSnapshot, afterSnapshot)
      command.doStep()
      command.undoStep()
      command.redoStep()
      controller.snapshot should be(afterSnapshot)
    }
  }