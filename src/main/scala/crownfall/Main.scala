package crownfall

import crownfall.aview.tui.*
import crownfall.model.*

// $COVERAGE-OFF$
  @main def hello(): Unit =
    println()
    println("---------------------")
    println("Welcome to Crownfall!")
    println("---------------------")
    println()
    Tui.run(Board.initial)
  // $COVERAGE-ON$