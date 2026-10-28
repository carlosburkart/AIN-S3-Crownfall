package crownfall.aview.tui

import crownfall.model.*

enum Command:
  case Move(from: Position, to: Position)
  case Quit