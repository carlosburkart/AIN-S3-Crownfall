package crownfall.aview.tui

import crownfall.controller.*
import crownfall.model.*

object InputParser:

  private val Coordinate = "([A-Za-z])(\\d{1,2})".r

  def parsePosition(input: String): Option[Position] =
    input.trim match
      case Coordinate(letter, number) => Some(Position(letter.toUpperCase.head - 'A', number.toInt - 1))
      case _                          => None

  def parseCommand(input: String): Option[Command] =
    input.trim.toLowerCase.split("\\s+").toList match
      case List("quit")           => Some(Command.Quit)
      case List("info", pos)      => parsePosition(pos).map(p => Command.Info(p))
      case List("help")           => Some(Command.Help)
      case List("move", from, to) =>
        for
          start  <- parsePosition(from)
          target <- parsePosition(to)
        yield Command.Move(start, target)
      case _                      => None