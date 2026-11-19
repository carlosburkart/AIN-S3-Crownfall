package crownfall.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RuleSpec extends AnyWordSpec with Matchers:

  private val knight = Troop.create(1, Side.One, TroopKind.Knight)
  private val archer = Troop.create(2, Side.One, TroopKind.Archer)
  private val friend = Troop.create(3, Side.One, TroopKind.Knight)
  private val enemy  = Troop.create(4, Side.Two, TroopKind.Knight)

  private val start = Position(3, 3)
  private val emptyBoard = Board(8, 8, Map())
  
  private object MustNotBeAsked extends Rule:
    def check(board: Board, from: Position, to: Position): Option[GameError] =
      fail("The chain did not stop at the first error")

  "The chain" should {

    "report no error if every rule is fulfilled" in {
      val board = Board(8, 8, Map(start -> knight))
      Rule.firstError(Board.moveRules, board, start, Position(3, 4)) should be(None)
    }

    "report the error of the first rule that fails" in {
      val outside = Position(-1, 0)
      Rule.firstError(List(TargetInside, TroopAtStart), emptyBoard, start, outside) should be(Some(GameError.OutOfBounds))
      Rule.firstError(List(TroopAtStart, TargetInside), emptyBoard, start, outside) should be(Some(GameError.NoTroopThere))
    }

    "stop at the first error and not ask the remaining rules" in {
      Rule.firstError(List(TroopAtStart, MustNotBeAsked), emptyBoard, start, Position(3, 4)) should be(Some(GameError.NoTroopThere))
    }

    "report no error for an empty chain" in {
      Rule.firstError(List(), emptyBoard, start, Position(3, 4)) should be(None)
    }
  }

  "TroopAtStart" should {

    "fail if there is no troop on the start field" in {
      TroopAtStart.check(emptyBoard, start, Position(3, 4)) should be(Some(GameError.NoTroopThere))
    }

    "pass if there is a troop on the start field" in {
      TroopAtStart.check(Board(8, 8, Map(start -> knight)), start, Position(3, 4)) should be(None)
    }
  }

  "TargetInside" should {

    "fail if the target is outside the board" in {
      TargetInside.check(emptyBoard, start, Position(8, 3)) should be(Some(GameError.OutOfBounds))
      TargetInside.check(emptyBoard, start, Position(3, -1)) should be(Some(GameError.OutOfBounds))
    }

    "pass if the target is on the board" in {
      TargetInside.check(emptyBoard, start, Position(7, 7)) should be(None)
    }
  }

  "TargetFree" should {

    "fail if another troop stands on the target" in {
      val board = Board(8, 8, Map(start -> knight, Position(3, 4) -> friend))
      TargetFree.check(board, start, Position(3, 4)) should be(Some(GameError.FieldOccupied))
    }

    "pass if the target is empty" in {
      TargetFree.check(Board(8, 8, Map(start -> knight)), start, Position(3, 4)) should be(None)
    }
  }

  "WithinMovement" should {

    "fail if the target is further away than the movement" in {
      WithinMovement.check(Board(8, 8, Map(start -> knight)), start, Position(3, 5)) should be(Some(GameError.OutOfRange))
    }

    "pass if the target is exactly at the movement" in {
      WithinMovement.check(Board(8, 8, Map(start -> knight)), start, Position(4, 4)) should be(None)
    }

    "leave a missing troop to another rule" in {
      WithinMovement.check(emptyBoard, start, Position(3, 7)) should be(None)
    }
  }

  "TargetPresent" should {

    "fail if there is no troop on the target" in {
      TargetPresent.check(Board(8, 8, Map(start -> knight)), start, Position(3, 4)) should be(Some(GameError.NoTarget))
    }

    "pass if there is a troop on the target" in {
      val board = Board(8, 8, Map(start -> knight, Position(3, 4) -> enemy))
      TargetPresent.check(board, start, Position(3, 4)) should be(None)
    }
  }

  "TargetIsEnemy" should {

    "fail if the target belongs to the same side" in {
      val board = Board(8, 8, Map(start -> knight, Position(3, 4) -> friend))
      TargetIsEnemy.check(board, start, Position(3, 4)) should be(Some(GameError.OwnTroop))
    }

    "pass if the target belongs to the opponent" in {
      val board = Board(8, 8, Map(start -> knight, Position(3, 4) -> enemy))
      TargetIsEnemy.check(board, start, Position(3, 4)) should be(None)
    }

    "leave a missing troop to another rule" in {
      TargetIsEnemy.check(Board(8, 8, Map(start -> knight)), start, Position(3, 4)) should be(None)
    }
  }

  "WithinRange" should {

    "fail if the target is further away than the range" in {
      val board = Board(8, 8, Map(start -> archer, Position(3, 7) -> enemy))
      WithinRange.check(board, start, Position(3, 7)) should be(Some(GameError.OutOfRange))
    }

    "pass if the target is exactly at the range" in {
      val board = Board(8, 8, Map(start -> archer, Position(3, 6) -> enemy))
      WithinRange.check(board, start, Position(3, 6)) should be(None)
    }

    "leave a missing troop to another rule" in {
      WithinRange.check(emptyBoard, start, Position(3, 7)) should be(None)
    }
  }