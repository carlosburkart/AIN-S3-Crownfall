package crownfall.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scala.util.{Failure, Success}

class ResultSpec extends AnyWordSpec with Matchers:

  private val error = GameError.NoTarget
  private val otherError = GameError.OutOfRange

  private val okValue: Result[Int] = Ok(2)
  private val errValue: Result[Int] = Err(error)

  "map" should {

    "apply the function to an Ok value" in {
      okValue.map(x => x + 1) should be(Ok(3))
    }

    "keep an Err unchanged" in {
      errValue.map(x => x + 1) should be(Err(error))
    }
  }

  "flatMap" should {

    "apply a function that returns a new Result" in {
      okValue.flatMap(x => Ok(x * 10)) should be(Ok(20))
    }

    "return the Err of the function" in {
      okValue.flatMap(_ => Err(otherError)) should be(Err(otherError))
    }

    "keep an Err unchanged and not call the function" in {
      errValue.flatMap(_ => fail("the function must not be called")) should be(Err(error))
    }
  }

  "filter" should {

    "keep an Ok value that fulfils the condition" in {
      Ok(5).filter(x => x > 3, error) should be(Ok(5))
    }

    "turn an Ok value that does not fulfil the condition into the given Err" in {
      Ok(1).filter(x => x > 3, error) should be(Err(error))
    }

    "keep an Err unchanged" in {
      errValue.filter(_ => true, otherError) should be(Err(error))
    }
  }

  "A for-comprehension over Results" should {

    "combine Ok values" in {
      val result = for
        a <- Ok(1)
        b <- Ok(2)
      yield a + b
      result should be(Ok(3))
    }

    "stop at the first Err" in {
      val result = for
        a <- Ok(1)
        b <- (Err(error): Result[Int])
        c <- (Err(otherError): Result[Int])
      yield a + b + c
      result should be(Err(error))
    }
  }

  "Result.fromOption" should {

    "turn Some into Ok" in {
      Result.fromOption(Some(1), error) should be(Ok(1))
    }

    "turn None into the given Err" in {
      Result.fromOption(None, error) should be(Err(error))
    }
  }

  "Result.fromTry" should {

    "turn a Success into Ok" in {
      Result.fromTry(Success(1), error) should be(Ok(1))
    }

    "turn a Failure into the given Err" in {
      Result.fromTry(Failure(IllegalStateException("broken")), error) should be(Err(error))
    }
  }

  "Result" should {

    // The three monad laws

    "fulfil left identity: Ok(a).flatMap(f) == f(a)" in {
      val f = (x: Int) => Ok(x * 3)
      Ok(2).flatMap(f) should be(f(2))
    }

    "fulfil right identity: m.flatMap(Ok(_)) == m" in {
      okValue.flatMap(x => Ok(x)) should be(okValue)
      errValue.flatMap(x => Ok(x)) should be(errValue)
    }

    "fulfil associativity: m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))" in {
      val f = (x: Int) => Ok(x + 1)
      val g = (x: Int) => if x > 2 then Ok(x * 2) else Err(error)
      for m <- List(okValue, errValue, Ok(0)) do
        m.flatMap(f).flatMap(g) should be(m.flatMap(x => f(x).flatMap(g)))
    }
  }