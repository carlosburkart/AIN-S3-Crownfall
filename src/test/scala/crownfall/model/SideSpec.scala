package crownfall.model

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class SideSpec extends AnyWordSpec with Matchers:

  "A Side" should {

    "have Two as the opponent of One" in {
      Side.One.opponent should be(Side.Two)
    }

    "have One as the opponent of Two" in {
      Side.Two.opponent should be(Side.One)
    }

    "be the opponent of its own opponent" in {
      for side <- Side.values do
        side.opponent.opponent should be(side)
    }
  }