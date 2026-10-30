package crownfall.aview.tui

import crownfall.model.*

object BoardRenderer:
  def symbol(troop: Troop): String =
    val letter = troop.kind match
      case TroopKind.Knight => "K"
      case TroopKind.Archer => "A"
      case TroopKind.Mage   => "M"
    if troop.owner == Side.One then letter else letter.toLowerCase

  def coloredSymbol(troop: Troop): String =
    val color = if troop.owner == Side.One then Console.BLUE else Console.RED
    color + symbol(troop) + Console.RESET

  def render(board: Board, useColor: Boolean = true): String =
    val header = "  " + (0 until board.cols).map(col => ('A' + col).toChar.toString).mkString(" ")
    val lines = (0 until board.rows).reverse.map(row => renderRow(board, row, useColor))
    (header +: lines).mkString("\n")

  private def renderRow(board: Board, row: Int, useColor: Boolean): String =
    val number = (row + 1).toString + " "
    val columns = 0 until board.cols
    val cells = columns.map(col => cell(board, Position(col, row), useColor))
    val joined = cells.mkString(" ")
    number + joined

  private def cell(board: Board, pos: Position, useColor: Boolean): String =
    board.troopAt(pos) match
      case None        => "."
      case Some(troop) => if useColor then coloredSymbol(troop) else symbol(troop)

  def troopInfo(troop: Troop): String =
    val kind = troop.kind
    val owner = troop.owner
    val hp = troop.hp
    val maxHp = troop.kind.maxHp
    val atk = troop.kind.attack
    val df = troop.kind.defense
    val rng = troop.kind.range
    val mov = troop.kind.movement
    s"$kind (Side $owner) | HP $hp/$maxHp | ATK $atk | DEF $df | RNG $rng | MOV $mov"
    
  def legend: String =
    TroopKind.values
      .map(kind => s"${symbol(Troop.create(0, Side.One, kind))} = $kind")
      .mkString(", ")
