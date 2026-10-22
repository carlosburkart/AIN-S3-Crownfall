package crownfall.model

case class Position(col: Int, row: Int):

  def distanceTo(other: Position): Int =
    val distanceCol = math.abs(col - other.col)
    val distanceRow = math.abs(row - other.row)
    math.max(distanceCol, distanceRow)

  def neighbors: Seq[Position] =
    for
      dc <- -1 to 1
      dr <- -1 to 1
      if !(dc == 0 && dr == 0)
    yield Position(col + dc, row + dr)