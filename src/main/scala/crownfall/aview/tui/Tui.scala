package crownfall.aview.tui

import crownfall.model.*
import scala.io.StdIn

object Tui:
  // $COVERAGE-OFF$
  def run(board: Board): Unit =
    println(BoardRenderer.render(board))
    println()
    println(BoardRenderer.legend)
    println()
    val input = StdIn.readLine("> ")
    InputParser.parseCommand(input) match
      case Some(Command.Quit) =>
        println()
        println("Thank you for playing our first demo!")
        println()
      case Some(Command.Help) =>
        println()
        println("Commands:")
        println("> move <from> <to>")
        println("> info <field>")
        println("> help")
        println("> quit")
        println()
        run(board)
      case Some(Command.Info(pos)) =>
        println()
        board.troopAt(pos) match {
          case Some(troop) => println(BoardRenderer.troopInfo(troop))
          case None => println("No troop on this field")
        }
        println()
        run(board)
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