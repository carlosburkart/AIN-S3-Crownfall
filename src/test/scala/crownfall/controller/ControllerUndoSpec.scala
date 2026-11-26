package crownfall.controller

import crownfall.model.*
import crownfall.util.CountingObserver
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ControllerUndoSpec extends AnyWordSpec with Matchers:

  private val knightPos = Position(0, 1)
  private val enemyPos  = Position(7, 7)
  private val knight = Troop.create(1, Side.One, TroopKind.Knight)
  private val enemy  = Troop.create(2, Side.Two, TroopKind.Knight)

  private def startBoard = Board(8, 8, Map(knightPos -> knight, enemyPos -> enemy))

  "A Controller" should {

    "undo a move" in {
      val controller = Controller(startBoard)
      controller.move(knightPos, Position(0, 2))
      controller.undo() should be(Ok(startBoard))
      controller.board should be(startBoard)
    }

    "give the turn back after an undo" in {
      val controller = Controller(startBoard)
      controller.move(knightPos, Position(0, 2))
      controller.undo()
      controller.state should be(Turn(Side.One))
    }

    "redo an undone move" in {
      val controller = Controller(startBoard)
      controller.move(knightPos, Position(0, 2))
      val afterMove = controller.board
      controller.undo()
      controller.redo() should be(Ok(afterMove))
      controller.state should be(Turn(Side.Two))
    }

    "bring back a defeated troop and continue the game after an undo" in {
      val archer = Troop.create(3, Side.One, TroopKind.Archer)
      val lastEnemy = Troop.create(4, Side.Two, TroopKind.Mage) // 4 hp, RatioDamage: 6 * 5 / 6 = 5
      val board = Board(8, 8, Map(Position(0, 1) -> archer, Position(0, 3) -> lastEnemy))
      val controller = Controller(board)
      controller.attack(Position(0, 1), Position(0, 3))
      controller.state should be(GameOver(Side.One))
      controller.undo()
      controller.board should be(board)
      controller.state should be(Turn(Side.One))
    }

    "report that there is nothing to undo at the start" in {
      Controller(startBoard).undo() should be(Err(GameError.NothingToUndo))
    }

    "report that there is nothing to redo at the start" in {
      Controller(startBoard).redo() should be(Err(GameError.NothingToRedo))
    }

    "not remember an action that failed" in {
      val controller = Controller(startBoard)
      controller.move(knightPos, Position(-1, 1))
      controller.undo() should be(Err(GameError.NothingToUndo))
    }

    "forget the undone actions after a new action" in {
      val controller = Controller(startBoard)
      controller.move(knightPos, Position(0, 2))
      controller.undo()
      controller.move(knightPos, Position(1, 2))
      controller.redo() should be(Err(GameError.NothingToRedo))
    }

    "notify observers after undo and redo" in {
      val observer = CountingObserver()
      val controller = Controller(startBoard)
      controller.move(knightPos, Position(0, 2))
      controller.add(observer)
      controller.undo()
      controller.redo()
      observer.count should be(2)
    }

    "not notify observers if there is nothing to undo or redo" in {
      val observer = CountingObserver()
      val controller = Controller(startBoard)
      controller.add(observer)
      controller.undo()
      controller.redo()
      observer.count should be(0)
    }

    "list the actions with side and fields" in {
      val controller = Controller(startBoard)
      controller.move(knightPos, Position(0, 2))
      controller.move(enemyPos, Position(7, 6))
      controller.history should be(List("Side One: move A2 -> A3", "Side Two: move H8 -> H7"))
    }

    "remove an undone action from the history" in {
      val controller = Controller(startBoard)
      controller.move(knightPos, Position(0, 2))
      controller.undo()
      controller.history should be(Nil)
    }
  }