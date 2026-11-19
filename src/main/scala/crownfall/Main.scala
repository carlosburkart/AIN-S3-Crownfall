package crownfall

import crownfall.aview.tui.{ColorRenderer, Tui}
import crownfall.controller.Controller
import crownfall.model.*

// $COVERAGE-OFF$
@main def hello(): Unit =
  println()
  println("---------------------")
  println("Welcome to Crownfall!")
  println("---------------------")
  val controller = Controller(Board.initial, RatioDamage)
  val tui = Tui(controller, ColorRenderer())
  controller.add(tui)
  tui.update()
  tui.run()
// $COVERAGE-ON$