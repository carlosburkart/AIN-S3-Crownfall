package crownfall.model

case class Board(cols: Int, rows: Int, troops: Map[Position, Troop]):
  require(cols > 0 && rows > 0, "A board must have a minimum of one column and one row")
  require(troops.keys.forall(isInside), "All troops must be placed inside the board")
  
  def isInside(pos: Position): Boolean =
    pos.col >= 0 && pos.col < cols
      && pos.row >= 0 && pos.row < rows


  def troopAt(pos: Position): Option[Troop] =
    troops.get(pos)