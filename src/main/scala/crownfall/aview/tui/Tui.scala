package crownfall.aview.tui

import crownfall.model.*
import scala.io.StdIn

object Tui:
  // $COVERAGE-OFF$
  def run(board: Board): Unit =
    println(BoardRenderer.render(board))
    println()
    val input = StdIn.readLine("> ")
    InputParser.parseCommand(input) match
      case Some(Command.Quit) =>
        println()
        println("Thank you for playing our first demo!")
        println()
      case Some(Command.Move(from, to)) =>
        board.move(from, to) match
          case Right(newBoard) =>
            println()
            run(newBoard)
          case Left(error) =>
            println()
            println(error)
            println()
            run(board)
      case None =>
        println()
        println("Unknown command: Use move <from> <to> or quit")
        println()
        run(board)
// $COVERAGE-ON$