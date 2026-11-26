package crownfall.aview.tui

import crownfall.controller.Input
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
      case Some(Input.Quit) =>
        println()
        println("Thank you for playing the demo!")
        println()
        false

      case Some(Input.Move(from, to)) =>
        handleResult(controller.move(from, to))

      case Some(Input.Attack(from, to)) =>
        handleResult(controller.attack(from, to))

      case Some(Input.Undo) =>
        handleResult(controller.undo())

      case Some(Input.Redo) =>
        handleResult(controller.redo())

      case Some(Input.Info(pos)) =>
        println()
        controller.board.troopAt(pos) match
          case Some(troop) => println(BoardRenderer.troopInfo(troop))
          case None => println(BoardRenderer.errorMessage(GameError.NoTroopThere))
        println()
        true

      case Some(Input.Help) =>
        println()
        println("Commands:")
        println("> move <from> <to>")
        println("> attack <from> <to>")
        println("> undo")
        println("> redo")
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
        
  private def handleResult(result: Result[Board]): Boolean =
    result match
      case Err(error) =>
        println()
        println(BoardRenderer.errorMessage(error))
        println()
        println()
      case Ok(_) =>
        ()
    true

  // $COVERAGE-OFF$
  @tailrec
  final def run(): Unit =
    Option(StdIn.readLine("> ")) match
      case Some(input) => if processInput(input) then run()
      case None        => println("Input ended, game is terminated.")
  // $COVERAGE-ON$