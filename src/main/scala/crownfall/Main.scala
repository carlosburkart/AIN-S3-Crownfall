package crownfall

import crownfall.aview.tui.BoardRenderer
import crownfall.model.*

// $COVERAGE-OFF$
@main def hello(): Unit =
  println()
  println("------------------------")
  println("Willkommen bei Crownfall")
  println("------------------------")
  println()
  println(BoardRenderer.render(Board.initial))
  println()
  // $COVERAGE-ON$