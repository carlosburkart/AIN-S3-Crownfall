package crownfall.aview.tui

import crownfall.model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RendererSpec extends AnyWordSpec with Matchers:

  private val initialBoard =
    """  A B C D E F G H
      |8 . m . . . . m .
      |7 k a k . . k a k
      |6 . k . . . . k .
      |5 . . . . . . . .
      |4 . . . . . . . .
      |3 . K . . . . K .
      |2 K A K . . K A K
      |1 . M . . . . M .""".stripMargin


  private def withoutColors(text: String): String =
    text.replace(Console.BLUE, "").replace(Console.RED, "").replace(Console.RESET, "")

  "A PlainRenderer" should {

    "render the initial board" in {
      PlainRenderer().render(Board.initial) should be(initialBoard)
    }

    "render a small board with its own size" in {
      val board = Board(2, 1, Map(Position(0, 0) -> Troop.create(1, Side.Two, TroopKind.Knight)))
      PlainRenderer().render(board) should be("  A B\n1 k .")
    }

    "not use any colors" in {
      val result = PlainRenderer().render(Board.initial)
      result should not include (Console.BLUE)
      result should not include (Console.RED)
    }
  }

  "A ColorRenderer" should {

    "show side One in blue and side Two in red" in {
      val result = ColorRenderer().render(Board.initial)
      result should include(Console.BLUE + "K" + Console.RESET)
      result should include(Console.RED + "k" + Console.RESET)
    }

    "keep the same layout as the PlainRenderer" in {
      withoutColors(ColorRenderer().render(Board.initial)) should be(initialBoard)
    }

    "not color empty fields" in {
      val board = Board(1, 1, Map())
      ColorRenderer().render(board) should be("  A\n1 .")
    }
  }