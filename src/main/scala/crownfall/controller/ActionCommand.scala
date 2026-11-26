package crownfall.controller

class ActionCommand(controller: Controller, before: Snapshot, after: Snapshot, val description: String = "") extends Command:
  def doStep(): Unit = controller.restore(after)
  def undoStep(): Unit = controller.restore(before)
  def redoStep(): Unit = controller.restore(after)
