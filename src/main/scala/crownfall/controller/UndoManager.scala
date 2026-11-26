package crownfall.controller

class UndoManager:
  private var undoStack: List[Command] = Nil
  private var redoStack: List[Command] = Nil

  def doStep(command: Command): Unit =
    command.doStep()
    undoStack = command :: undoStack
    redoStack = Nil

  def undoStep(): Option[Command] =
    undoStack match
      case Nil          => None
      case head :: rest =>
        head.undoStep()
        undoStack = rest
        redoStack = head :: redoStack
        Some(head)

  def redoStep(): Option[Command] =
    redoStack match
      case Nil => None
      case head :: rest =>
        head.redoStep()
        redoStack = rest
        undoStack = head :: undoStack
        Some(head)