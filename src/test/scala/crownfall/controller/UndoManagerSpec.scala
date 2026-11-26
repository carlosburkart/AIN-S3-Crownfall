package crownfall.controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UndoManagerSpec extends AnyWordSpec with Matchers:

  // A command only for tests: it writes down which of its steps were called.
  private class RecordingCommand(name: String, log: StringBuilder) extends Command:
    def doStep(): Unit = log.append(s"do $name;")
    def undoStep(): Unit = log.append(s"undo $name;")
    def redoStep(): Unit = log.append(s"redo $name;")
    def description: String = name

  "An UndoManager" should {

    "execute a command" in {
      val log = StringBuilder()
      UndoManager().doStep(RecordingCommand("a", log))
      log.toString should be("do a;")
    }

    "undo the last command" in {
      val log = StringBuilder()
      val manager = UndoManager()
      val command = RecordingCommand("a", log)
      manager.doStep(command)
      manager.undoStep() should be(Some(command))
      log.toString should be("do a;undo a;")
    }

    "undo the commands in reverse order" in {
      val log = StringBuilder()
      val manager = UndoManager()
      manager.doStep(RecordingCommand("a", log))
      manager.doStep(RecordingCommand("b", log))
      manager.undoStep()
      manager.undoStep()
      log.toString should be("do a;do b;undo b;undo a;")
    }

    "have nothing to undo at the start" in {
      UndoManager().undoStep() should be(None)
    }

    "have nothing to undo after everything was undone" in {
      val manager = UndoManager()
      manager.doStep(RecordingCommand("a", StringBuilder()))
      manager.undoStep()
      manager.undoStep() should be(None)
    }

    "redo the last undone command" in {
      val log = StringBuilder()
      val manager = UndoManager()
      val command = RecordingCommand("a", log)
      manager.doStep(command)
      manager.undoStep()
      manager.redoStep() should be(Some(command))
      log.toString should be("do a;undo a;redo a;")
    }

    "have nothing to redo at the start" in {
      UndoManager().redoStep() should be(None)
    }

    "be able to undo a command again after it was redone" in {
      val log = StringBuilder()
      val manager = UndoManager()
      manager.doStep(RecordingCommand("a", log))
      manager.undoStep()
      manager.redoStep()
      manager.undoStep()
      log.toString should be("do a;undo a;redo a;undo a;")
    }

    "forget the undone commands when a new command is executed" in {
      val manager = UndoManager()
      manager.doStep(RecordingCommand("a", StringBuilder()))
      manager.undoStep()
      manager.doStep(RecordingCommand("b", StringBuilder()))
      manager.redoStep() should be(None)
    }

    "list the commands that can be undone, the oldest first" in {
      val manager = UndoManager()
      val a = RecordingCommand("a", StringBuilder())
      val b = RecordingCommand("b", StringBuilder())
      manager.doStep(a)
      manager.doStep(b)
      manager.history should be(List(a, b))
      manager.undoStep()
      manager.history should be(List(a))
    }
  }