package crownfall.aview.tui

import crownfall.controller.Input
import crownfall.model.Position

object InputParser:

  private val Coordinate = "([A-Za-z])(\\d{1,2})".r

  def parsePosition(input: String): Option[Position] =
    input.trim match
      case Coordinate(letter, number) => Some(Position(letter.toUpperCase.head - 'A', number.toInt - 1))
      case _                          => None

  def parseCommand(input: String): Option[Input] =
    input.trim.toLowerCase.split("\\s+").toList match
      case List("quit")             => Some(Input.Quit)
      case List("info", pos)        => parsePosition(pos).map(p => Input.Info(p))
      case List("help")             => Some(Input.Help)
      case List("undo")             => Some(Input.Undo)
      case List("redo")             => Some(Input.Redo)
      case List("move", from, to)   =>
        for
          start  <- parsePosition(from)
          target <- parsePosition(to)
        yield Input.Move(start, target)
      case List("attack", from, to) =>
        for
          start <- parsePosition(from)
          target <- parsePosition(to)
        yield Input.Attack(start, target)
      case List("history") => Some(Input.History)
      case _                        => None