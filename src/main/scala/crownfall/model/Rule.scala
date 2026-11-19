package crownfall.model

trait Rule:
  def check(board: Board, from: Position, to: Position): Option[GameError]

object Rule:
  def firstError(rules: List[Rule], board: Board, from: Position, to: Position): Option[GameError] =
    rules.view.flatMap(rule => rule.check(board, from, to)).headOption

// Rules for move and attack
object TroopAtStart extends Rule:
  def check(board: Board, from: Position, to: Position): Option[GameError] =
    if board.troopAt(from).isEmpty then Some(GameError.NoTroopThere) else None

// Rules for move
object TargetInside extends Rule:
  def check(board: Board, from: Position, to: Position): Option[GameError] =
    if !board.isInside(to) then Some(GameError.OutOfBounds) else None

object TargetFree extends Rule:
  def check(board: Board, from: Position, to: Position): Option[GameError] =
    if board.troopAt(to).isDefined then Some(GameError.FieldOccupied) else None

object WithinMovement extends Rule:
  def check(board: Board, from: Position, to: Position): Option[GameError] =
    val tooFar = board.troopAt(from).exists(troop => from.distanceTo(to) > troop.kind.movement)
    if tooFar then Some(GameError.OutOfRange) else None

// Rules for attack
object TargetPresent extends Rule:
  def check(board: Board, from: Position, to: Position): Option[GameError] =
    if board.troopAt(to).isEmpty then Some(GameError.NoTarget) else None

object TargetIsEnemy extends Rule:
  def check(board: Board, from: Position, to: Position): Option[GameError] =
    (board.troopAt(from), board.troopAt(to)) match
      case (Some(attacker), Some(defender)) if attacker.owner == defender.owner => Some(GameError.OwnTroop)
      case _                                                                    => None

object WithinRange extends Rule:
  def check(board: Board, from: Position, to: Position): Option[GameError] =
    val tooFar = board.troopAt(from).exists(troop => from.distanceTo(to) > troop.kind.range)
    if tooFar then Some(GameError.OutOfRange) else None