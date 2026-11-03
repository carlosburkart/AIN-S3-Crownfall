package crownfall.controller

import crownfall.model.*
import crownfall.util.Observable

class Controller(initialBoard: Board) extends Observable:

  private var current: Board = initialBoard

  def board: Board = current

  def move(from: Position, to: Position): Either[String, Board] =
    val result = current.move(from, to)
    result match
      case Right(newBoard) =>
        current = newBoard
        notifyObservers()
      case Left(_) =>
        ()
    result