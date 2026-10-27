package crownfall.aview.tui

import crownfall.model.*

object BoardRenderer:
  def symbol(troop: Troop): String =
    val letter = troop.kind match
      case TroopKind.Knight => "K"
      case TroopKind.Archer => "A"
      case TroopKind.Mage   => "M"
    if troop.owner == Side.One then letter else letter.toLowerCase

  def render(board: Board): String =
    val header = "  " + (0 until board.cols).map(col => ('A' + col).toChar.toString).mkString(" ")
    val lines = (0 until board.rows).reverse.map(row => renderRow(board, row))
    (header +: lines).mkString("\n")

  private def renderRow(board: Board, row: Int): String =
    val number = (row + 1).toString + " "
    val columns = 0 until board.cols
    val cells = columns.map(col => cell(board, Position(col, row)))
    val joined = cells.mkString(" ")
    number + joined

  private def cell(board: Board, pos: Position): String =
    board.troopAt(pos) match
      case None        => "."
      case Some(troop) => symbol(troop)