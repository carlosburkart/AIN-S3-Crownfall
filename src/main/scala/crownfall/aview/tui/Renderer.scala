package crownfall.aview.tui

import crownfall.model.*

abstract class Renderer:

  final def render(board: Board): String =
    val header = "  " + (0 until board.cols).map(col => ('A' + col).toChar.toString).mkString(" ")
    val lines = (0 until board.rows).reverse.map(row => renderRow(board, row))
    (header +: lines).mkString("\n")

  private def renderRow(board: Board, row: Int): String =
    val cells = (0 until board.cols).map(col => cell(board.troopAt(Position(col, row))))
    s"${row + 1} " + cells.mkString(" ")

  private def cell(troop: Option[Troop]): String = troop match
    case None     => emptyCell
    case Some(t)  => troopCell(t)

  protected def emptyCell: String = "."

  protected def troopCell(troop: Troop): String

class PlainRenderer extends Renderer:
  protected def troopCell(troop: Troop): String = BoardRenderer.symbol(troop)

class ColorRenderer extends Renderer:
  protected def troopCell(troop: Troop): String = BoardRenderer.coloredSymbol(troop)