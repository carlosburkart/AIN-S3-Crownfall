package crownfall.controller

import crownfall.model.*

enum Input:
  case Move(from: Position, to: Position)
  case Attack(from: Position, to: Position)
  case Undo
  case Redo
  case Quit
  case Help
  case Info(pos: Position)