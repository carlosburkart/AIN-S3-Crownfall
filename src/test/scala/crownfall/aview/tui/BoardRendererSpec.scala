package crownfall.aview.tui

import crownfall.model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BoardRendererSpec extends AnyWordSpec with Matchers:

  "A BoardRenderer" should {

    "show K, A and M for troops of side One" in {
      BoardRenderer.symbol(Troop.create(1, Side.One, TroopKind.Knight)) should be("K")
      BoardRenderer.symbol(Troop.create(1, Side.One, TroopKind.Archer)) should be("A")
      BoardRenderer.symbol(Troop.create(1, Side.One, TroopKind.Mage)) should be("M")
    }

    "show the same letters in lower case for side Two" in {
      for kind <- TroopKind.values do
        val one = Troop.create(1, Side.One, kind)
        val two = Troop.create(2, Side.Two, kind)
        BoardRenderer.symbol(two) should be(BoardRenderer.symbol(one).toLowerCase)
    }

    "color troops of side One blue" in {
      val troop = Troop.create(1, Side.One, TroopKind.Knight)
      val result = BoardRenderer.coloredSymbol(troop)
      result should be(Console.BLUE + "K" + Console.RESET)
    }

    "color troops of side Two red" in {
      val troop = Troop.create(2, Side.Two, TroopKind.Knight)
      val result = BoardRenderer.coloredSymbol(troop)
      result should be(Console.RED + "k" + Console.RESET)
    }

    "describe a troop with its stats" in {
      val troop = Troop.create(1, Side.One, TroopKind.Knight)
      val result = BoardRenderer.troopInfo(troop)
      result should be("Knight (Side One) | HP 10/10 | ATK 4 | DEF 5 | RNG 1 | MOV 1")
    }

    "show a legend with all troop kinds" in {
      val result = BoardRenderer.legend
      result should include("K = Knight")
      result should include("A = Archer")
      result should include("M = Mage")
      result should include(", ")
    }

    "have an error message for all GameErrors" in {
      for error <- GameError.values do
        BoardRenderer.errorMessage(error) should not be empty
    }

    "have a different message for every game error" in {
      val messages = GameError.values.map(BoardRenderer.errorMessage)
      messages.toSet.size should be(messages.size)
    }
  }

  "The state info" should {

    "name the side that is on turn" in {
      BoardRenderer.stateInfo(Turn(Side.One)) should include("One")
      BoardRenderer.stateInfo(Turn(Side.Two)) should include("Two")
    }

    "name the winner when the game is over" in {
      BoardRenderer.stateInfo(GameOver(Side.Two)) should include("Two")
    }

    "differ between a turn and the end of the game" in {
      BoardRenderer.stateInfo(Turn(Side.One)) should not be (BoardRenderer.stateInfo(GameOver(Side.One)))
    }
  }

  "The attack report" should {

    val archer = Troop.create(1, Side.One, TroopKind.Archer)
    val knight = Troop.create(2, Side.Two, TroopKind.Knight)
    val from = Position(0, 1)
    val to = Position(0, 3)
    val boardBefore = Board(8, 8, Map(from -> archer, to -> knight))

    "name the damage and the hit points left" in {
      val boardAfter = Board(8, 8, Map(from -> archer, to -> knight.copy(hp = 7)))
      BoardRenderer.attackReport(boardBefore, boardAfter, from, to) should be(
        "Archer hits Knight for 3 damage. Knight has 7 HP left.")
    }

    "say when the defender is defeated" in {
      val boardAfter = Board(8, 8, Map(from -> archer))
      BoardRenderer.attackReport(boardBefore, boardAfter, from, to) should be("Archer defeats Knight.")
    }

    "be empty if there was no attacker or defender" in {
      val emptyBoard = Board(8, 8, Map())
      BoardRenderer.attackReport(emptyBoard, emptyBoard, from, to) should be("")
    }
  }