package crownfall.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameStateSpec extends AnyWordSpec with Matchers:

  private val troopOne = Troop.create(1, Side.One, TroopKind.Knight)
  private val troopTwo = Troop.create(2, Side.Two, TroopKind.Knight)

  private val bothSides = Board(8, 8, Map(Position(0, 0) -> troopOne, Position(7, 7) -> troopTwo))
  private val onlyOne   = Board(8, 8, Map(Position(0, 0) -> troopOne))
  private val onlyTwo   = Board(8, 8, Map(Position(7, 7) -> troopTwo))

  "A Turn" should {

    "let a troop of its own side act" in {
      Turn(Side.One).check(troopOne) should be(None)
      Turn(Side.Two).check(troopTwo) should be(None)
    }

    "not let a troop of the other side act" in {
      Turn(Side.One).check(troopTwo) should be(Some(GameError.NotYourTroop))
      Turn(Side.Two).check(troopOne) should be(Some(GameError.NotYourTroop))
    }

    "pass the turn to the opponent if both sides have troops left" in {
      Turn(Side.One).next(bothSides) should be(Turn(Side.Two))
      Turn(Side.Two).next(bothSides) should be(Turn(Side.One))
    }

    "end the game if the opponent has no troops left" in {
      Turn(Side.One).next(onlyOne) should be(GameOver(Side.One))
      Turn(Side.Two).next(onlyTwo) should be(GameOver(Side.Two))
    }
  }

  "A GameOver" should {

    "not let any troop act" in {
      GameOver(Side.One).check(troopOne) should be(Some(GameError.GameIsOver))
      GameOver(Side.One).check(troopTwo) should be(Some(GameError.GameIsOver))
    }

    "stay over" in {
      GameOver(Side.One).next(bothSides) should be(GameOver(Side.One))
    }
  }