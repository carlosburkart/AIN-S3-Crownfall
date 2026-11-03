package crownfall

import crownfall.aview.tui.Tui
import crownfall.controller.Controller
import crownfall.model.*

// $COVERAGE-OFF$
  @main def hello(): Unit =
    println()
    println("---------------------")
    println("Welcome to Crownfall!")
    println("---------------------")
    val controller = Controller(Board.initial)
    val tui = Tui(controller)
    controller.add(tui)
    tui.update()
    tui.run()
  // $COVERAGE-ON$