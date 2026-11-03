package crownfall.aview.tui

import crownfall.controller.Command
import crownfall.controller.Controller
import crownfall.util.Observer
import scala.annotation.tailrec
import scala.io.StdIn

class Tui(controller: Controller) extends Observer:
  // $COVERAGE-OFF$
  def update(): Unit =
    println()
    println(BoardRenderer.render(controller.board))
    println()
    println(BoardRenderer.legend)
    println()
  // $COVERAGE-ON$

  def processInput(input: String): Boolean =
    InputParser.parseCommand(input) match
      case Some(Command.Quit) =>
        println()
        println("Thank you for playing the demo!")
        println()
        false

      case Some(Command.Move(from, to)) =>
        controller.move(from, to) match
          case Left(error) =>
            println()
            println(error)
            println()
          case Right(_) =>
            ()
        true

      case Some(Command.Info(pos)) =>
        println()
        controller.board.troopAt(pos) match
          case Some(troop) => println(BoardRenderer.troopInfo(troop))
          case None => println("No troop on this field")
        println()
        true

      case Some(Command.Help) =>
        println()
        println("Commands:")
        println("> move <from> <to>")
        println("> info <field>")
        println("> help")
        println("> quit")
        println()
        true

      case None =>
        println()
        println("Unknown command. Type 'help' for a list of commands.")
        println()
        true

  // $COVERAGE-OFF$
  @tailrec
  final def run(): Unit =
    val input = StdIn.readLine("> ")
    if processInput(input) then run()
  // $COVERAGE-ON$