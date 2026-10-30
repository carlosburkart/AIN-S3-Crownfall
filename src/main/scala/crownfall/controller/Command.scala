package crownfall.controller

import crownfall.model.*

enum Command:
  case Move(from: Position, to: Position)
  case Quit
  case Help
  case Info(pos: Position)