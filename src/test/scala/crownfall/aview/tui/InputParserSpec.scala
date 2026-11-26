package crownfall.aview.tui

import crownfall.controller.*
import crownfall.model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class InputParserSpec extends AnyWordSpec with Matchers:

  "An InputParser" should {

    "parse A1 as the bottom left corner" in {
      val input = "A1"
      val result = InputParser.parsePosition(input)
      result should be(Some(Position(0, 0)))
    }

    "parse C4 as column 2, row 3" in {
      val input = "C4"
      val result = InputParser.parsePosition(input)
      result should be(Some(Position(2, 3)))
    }

    "accept lower case letters" in {
      val input = "c4"
      val result = InputParser.parsePosition(input)
      result should be(Some(Position(2, 3)))
    }

    "reject input without a number" in {
      val input = "C"
      val result = InputParser.parsePosition(input)
      result should be(None)
    }

    "reject input with a number in front of a letter" in {
      val input = "4C"
      val result = InputParser.parsePosition(input)
      result should be(None)
    }

    "reject empty input" in {
      val input = ""
      val result = InputParser.parsePosition(input)
      result should be(None)
    }

    "reject inputs with more than two digits" in {
      val input = "C123"
      val result = InputParser.parsePosition(input)
      result should be(None)
    }

    "parse a move command with two coordinates" in {
      val input = "move C2 C3"
      val result = InputParser.parseCommand(input)
      result should be(Some(Input.Move(Position(2, 1), Position(2, 2))))
    }

    "parse the quit command" in {
      val input = "quit"
      val result = InputParser.parseCommand(input)
      result should be(Some(Input.Quit))
    }

    "reject a move command without a target" in {
      val input = "move C2"
      val result = InputParser.parseCommand(input)
      result should be(None)
    }

    "reject a move command with an invalid coordinate" in {
      val input = "move C2 Z"
      val result = InputParser.parseCommand(input)
      result should be(None)
    }

    "reject unknown commands" in {
      val input = "jump C2 C3"
      val result = InputParser.parseCommand(input)
      result should be(None)
    }

    "ignore upper and lower case in commands" in {
      val input = "MOVE C2 C3"
      val result = InputParser.parseCommand(input)
      result should be(Some(Input.Move(Position(2, 1), Position(2, 2))))
    }

    "parse an info command with a coordinate" in {
      val input = "info C2"
      val result = InputParser.parseCommand(input)
      result should be(Some(Input.Info(Position(2, 1))))
    }

    "reject an info command with an invalid coordinate" in {
      val input = "info Z"
      val result = InputParser.parseCommand(input)
      result should be(None)
    }

    "parse a help command" in {
      val input = "help"
      val result = InputParser.parseCommand(input)
      result should be(Some(Input.Help))
    }

    "parse an attack command with two coordinates" in {
      val input = "attack C2 C4"
      val result = InputParser.parseCommand(input)
      result should be(Some(Input.Attack(Position(2, 1), Position(2, 3))))
    }

    "reject an attack command without a target" in {
      val input = "attack C2"
      val result = InputParser.parseCommand(input)
      result should be(None)
    }

    "reject an attack command with an invalid coordinate" in {
      val input = "attack C2 Z"
      val result = InputParser.parseCommand(input)
      result should be(None)
    }

    "parse the undo command" in {
      InputParser.parseCommand("undo") should be(Some(Input.Undo))
    }

    "parse the redo command" in {
      InputParser.parseCommand("REDO") should be(Some(Input.Redo))
    }

    "parse the history command" in {
      InputParser.parseCommand("history") should be(Some(Input.History))
    }
  }