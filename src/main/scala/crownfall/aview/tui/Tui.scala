package crownfall.aview.tui

import crownfall.controller.Command
import crownfall.controller.Controller
import crownfall.util.Observer
import crownfall.model.*
import scala.annotation.tailrec
import scala.io.StdIn

class Tui(controller: Controller, renderer: Renderer = ColorRenderer()) extends Observer:
  // $COVERAGE-OFF$
  def update(): Unit =
    println()
    println(renderer.render(controller.board))
    println()
    println(BoardRenderer.legend)
    println(BoardRenderer.stateInfo(controller.state))
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
        handleResult(controller.move(from, to))

      case Some(Command.Attack(from, to)) =>
        handleResult(controller.attack(from, to))

      case Some(Command.Info(pos)) =>
        println()
        controller.board.troopAt(pos) match
          case Some(troop) => println(BoardRenderer.troopInfo(troop))
          case None => println(BoardRenderer.errorMessage(GameError.NoTroopThere))
        println()
        true

      case Some(Command.Help) =>
        println()
        println("Commands:")
        println("> move <from> <to>")
        println("> attack <from> <to>")
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
        
  private def handleResult(result: Either[GameError, Board]): Boolean =
    result match
      case Left(error) =>
        println()
        println(BoardRenderer.errorMessage(error))
        println()
        true
      case Right(_) =>
        controller.state match
          case GameOver(_) =>
            println()
            println("Thank you for playing the demo!")
            println()
            false
          case Turn(_) =>
            true

  // $COVERAGE-OFF$
  @tailrec
  final def run(): Unit =
    val input = StdIn.readLine("> ")
    if processInput(input) then run()
  // $COVERAGE-ON$