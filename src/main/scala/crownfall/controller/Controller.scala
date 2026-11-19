package crownfall.controller

import crownfall.model.*
import crownfall.util.Observable

class Controller(initialBoard: Board) extends Observable:

  private var current: Board = initialBoard

  private var currentState: GameState = Turn(Side.One)

  def state: GameState = currentState

  def board: Board = current

  private def turnError(from: Position): Option[GameError] =
    current.troopAt(from).flatMap(troop => currentState.check(troop))

  def move(from: Position, to: Position): Either[GameError, Board] =
    turnError(from) match
      case Some(error) => Left(error)
      case None        =>
        val result = current.move(from, to)
        result match
          case Right(newBoard) =>
            current = newBoard
            currentState = currentState.next(newBoard)
            notifyObservers()
          case Left(_) =>
            ()
        result