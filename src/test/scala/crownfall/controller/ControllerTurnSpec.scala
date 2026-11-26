package crownfall.controller

import crownfall.model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ControllerTurnSpec extends AnyWordSpec with Matchers:

  private val knightOne = Troop.create(1, Side.One, TroopKind.Knight)
  private val knightTwo = Troop.create(2, Side.Two, TroopKind.Knight)
  private val posOne = Position(0, 1)
  private val posTwo = Position(7, 6)

  private def bothSides = Board(8, 8, Map(posOne -> knightOne, posTwo -> knightTwo))

  "A Controller" should {

    "start with side One on turn" in {
      Controller(bothSides).state should be(Turn(Side.One))
    }

    "give the turn to side Two after a valid move" in {
      val controller = Controller(bothSides)
      controller.move(posOne, Position(0, 2))
      controller.state should be(Turn(Side.Two))
    }

    "give the turn back to side One after side Two has moved" in {
      val controller = Controller(bothSides)
      controller.move(posOne, Position(0, 2))
      controller.move(posTwo, Position(7, 5))
      controller.state should be(Turn(Side.One))
    }

    "keep the turn after an invalid move" in {
      val controller = Controller(bothSides)
      controller.move(posOne, Position(-1, 1))
      controller.state should be(Turn(Side.One))
    }

    "not move a troop of the side that is not on turn" in {
      val controller = Controller(bothSides)
      val result = controller.move(posTwo, Position(7, 5))
      result should be(Err(GameError.NotYourTroop))
      controller.board should be(bothSides)
      controller.state should be(Turn(Side.One))
    }

    "end the game when the opponent has no troops left" in {
      val onlyOne = Board(8, 8, Map(posOne -> knightOne))
      val controller = Controller(onlyOne)
      controller.move(posOne, Position(0, 2))
      controller.state should be(GameOver(Side.One))
    }

    "not allow any move after the game is over" in {
      val onlyOne = Board(8, 8, Map(posOne -> knightOne))
      val controller = Controller(onlyOne)
      controller.move(posOne, Position(0, 2))
      val result = controller.move(Position(0, 2), Position(0, 3))
      result should be(Err(GameError.GameIsOver))
    }
  }