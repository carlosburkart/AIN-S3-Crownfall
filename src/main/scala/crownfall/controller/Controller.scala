package crownfall.controller

import crownfall.model.*
import crownfall.util.Observable

class Controller(initialBoard: Board, strategy: DamageStrategy = RatioDamage) extends Observable:

  private var current: Board = initialBoard

  private var currentState: GameState = Turn(Side.One)

  private val undoManager = UndoManager()

  def state: GameState = currentState

  def board: Board = current

  def move(from: Position, to: Position): Result[Board] =
    act(from, s"move ${from.notation} -> ${to.notation}", current.move(from, to))


  def attack(from: Position, to: Position): Result[Board] =
    act(from, s"attack ${from.notation} -> ${to.notation}", current.attack(from, to, strategy))

  def undo(): Result[Board] =
    undoManager.undoStep() match
      case Some(_) =>
        notifyObservers()
        Ok(current)
      case None    =>
        Err(GameError.NothingToUndo)

  def redo(): Result[Board] =
    undoManager.redoStep() match
      case Some(_) =>
        notifyObservers()
        Ok(current)
      case None    =>
        Err(GameError.NothingToRedo)

  def history: List[String] = undoManager.history.map(command => command.description)

  private[controller] def snapshot: Snapshot = Snapshot(current, currentState)

  private[controller] def restore(snapshot: Snapshot): Unit =
    current = snapshot.board
    currentState = snapshot.state

  private def turnError(from: Position): Option[GameError] =
    current.troopAt(from).flatMap(troop => currentState.check(troop))

  private def act(from: Position, description: String, action: => Result[Board]): Result[Board] =
    turnError(from) match
      case Some(error) => Err(error)
      case None        =>
        val side = current.troopAt(from).map(troop => s"Side ${troop.owner}: ").getOrElse("")
        val result = action
        result match
          case Ok(newBoard) =>
            val before = snapshot
            val after = Snapshot(newBoard, currentState.next(newBoard))
            undoManager.doStep(ActionCommand(this, before, after, side + description))
            notifyObservers()
          case Err(_) =>
            ()
        result