package crownfall.controller

import crownfall.model.*
import crownfall.util.Observable

class Controller(initialBoard: Board, strategy: DamageStrategy = RatioDamage) extends Observable:

  private var current: Board = initialBoard

  private var currentState: GameState = Turn(Side.One)

  def state: GameState = currentState

  def board: Board = current

  def move(from: Position, to: Position): Either[GameError, Board] =
    act(from, current.move(from, to))

  def attack(from: Position, to: Position): Either[GameError, Board] =
    act(from, current.attack(from, to, strategy))

  private def turnError(from: Position): Option[GameError] =
    current.troopAt(from).flatMap(troop => currentState.check(troop))

  private def act(from: Position, action: => Either[GameError, Board]): Either[GameError, Board] =
    turnError(from) match
      case Some(error) => Left(error)
      case None        =>
        val result = action
        result match
          case Right(newBoard) =>
            current = newBoard
            currentState = currentState.next(newBoard)
            notifyObservers()
          case Left(_) =>
            ()
        result