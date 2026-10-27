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

    "render an empty board" in {
      val board = Board(3, 2, Map())
      val expected =
        """  A B C
          |2 . . .
          |1 . . .""".stripMargin
      val result = BoardRenderer.render(board)
      result should be(expected)
    }

    "render troops with their symbols" in {
      val troopOne = Troop.create(1, Side.One, TroopKind.Knight)
      val troopTwo = Troop.create(2, Side.Two, TroopKind.Knight)
      val posOne = Position(0, 0)
      val posTwo = Position(2, 1)
      val board = Board(3, 2, Map(posOne -> troopOne, posTwo -> troopTwo))
      val expected =
        """  A B C
          |2 . . k
          |1 K . .""".stripMargin
      val result = BoardRenderer.render(board)
      result should be(expected)
    }
  }