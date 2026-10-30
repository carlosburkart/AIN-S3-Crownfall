package crownfall.controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import crownfall.model.*

class ControllerSpec extends AnyWordSpec with Matchers:

  "A Controller" should {

    "start with the given board" in {
      val board = Board.initial
      
    }

    "update the board after a valid move" in {

    }

    "keep the board after an invalid move" in {

    }

    "notify observers after a valid move" in {

    }

    "not notify observers after an invalid move" in {
      
    }
  }