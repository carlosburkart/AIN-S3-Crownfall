package crownfall.model

case class Board(cols: Int, rows: Int, troops: Map[Position, Troop]):
  require(cols > 0 && rows > 0, "A board must have a minimum of one column and one row")
  require(troops.keys.forall(isInside), "All troops must be placed inside the board")

  def isInside(pos: Position): Boolean =
    pos.col >= 0 && pos.col < cols
      && pos.row >= 0 && pos.row < rows


  def troopAt(pos: Position): Option[Troop] =
    troops.get(pos)

  def move(from: Position, to: Position): Either[String, Board] =
    troopAt(from) match
      case None                                                     => Left("No troop available on this field")
      case Some(_) if !isInside(to)                                 => Left("You can't move troops outside the board")
      case Some(_) if troopAt(to).isDefined                         => Left("Another troop occupies this field")
      case Some(troop) if from.distanceTo(to) > troop.kind.movement => Left("This troop can't move this far")
      case Some(troop)                                              => Right(copy(troops = (troops - from) + (to -> troop)))

object Board:
  def initial: Board =
    Board(8, 8, Map(
      // player one

      // back row
      Position(1, 0) -> Troop.create(1, Side.One, TroopKind.Mage),
      Position(6, 0) -> Troop.create(2, Side.One, TroopKind.Mage),
      // second row
      Position(0, 1) -> Troop.create(3, Side.One, TroopKind.Knight),
      Position(1, 1) -> Troop.create(4, Side.One, TroopKind.Archer),
      Position(2, 1) -> Troop.create(5, Side.One, TroopKind.Knight),
      Position(5, 1) -> Troop.create(6, Side.One, TroopKind.Knight),
      Position(6, 1) -> Troop.create(7, Side.One, TroopKind.Archer),
      Position(7, 1) -> Troop.create(8, Side.One, TroopKind.Knight),
      // third row
      Position(1, 2) -> Troop.create(9, Side.One, TroopKind.Knight),
      Position(6, 2) -> Troop.create(10, Side.One, TroopKind.Knight),

      // player two

      // back row
      Position(1, 7) -> Troop.create(11, Side.Two, TroopKind.Mage),
      Position(6, 7) -> Troop.create(12, Side.Two, TroopKind.Mage),
      // second row
      Position(0, 6) -> Troop.create(13, Side.Two, TroopKind.Knight),
      Position(1, 6) -> Troop.create(14, Side.Two, TroopKind.Archer),
      Position(2, 6) -> Troop.create(15, Side.Two, TroopKind.Knight),
      Position(5, 6) -> Troop.create(16, Side.Two, TroopKind.Knight),
      Position(6, 6) -> Troop.create(17, Side.Two, TroopKind.Archer),
      Position(7, 6) -> Troop.create(18, Side.Two, TroopKind.Knight),
      // third row
      Position(1, 5) -> Troop.create(19, Side.Two, TroopKind.Knight),
      Position(6, 5) -> Troop.create(20, Side.Two, TroopKind.Knight)
    ))