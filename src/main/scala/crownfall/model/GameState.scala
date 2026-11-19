package crownfall.model

sealed trait GameState:
  def check(troop: Troop): Option[GameError]

  def next(board: Board): GameState

case class Turn(side: Side) extends GameState:
  def check(troop: Troop): Option[GameError] =
    if troop.owner != side then Some(GameError.NotYourTroop) else None

  def next(board: Board): GameState =
    val opponentHasTroops = board.troops.values.exists(t => t.owner == side.opponent)
    if opponentHasTroops then Turn(side.opponent) else GameOver(side)

case class GameOver(winner: Side) extends GameState:
  def check(troop: Troop): Option[GameError] =
    Some(GameError.GameIsOver)

  def next(board: Board): GameState =
    GameOver(winner)
