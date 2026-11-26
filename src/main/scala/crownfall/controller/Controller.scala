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
    act(from, current.move(from, to))

  def attack(from: Position, to: Position): Result[Board] =
    act(from, current.attack(from, to, strategy))

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

  private[controller] def snapshot: Snapshot = Snapshot(current, currentState)

  private[controller] def restore(snapshot: Snapshot): Unit =
    current = snapshot.board
    currentState = snapshot.state

  private def turnError(from: Position): Option[GameError] =
    current.troopAt(from).flatMap(troop => currentState.check(troop))

  private def act(from: Position, action: => Result[Board]): Result[Board] =
    turnError(from) match
      case Some(error) => Err(error)
      case None        =>
        val result = action
        result match
          case Ok(newBoard) =>
            val before = snapshot
            val after = Snapshot(newBoard, currentState.next(newBoard))
            undoManager.doStep(ActionCommand(this, before, after))
            notifyObservers()
          case Err(_) =>
            ()
        result