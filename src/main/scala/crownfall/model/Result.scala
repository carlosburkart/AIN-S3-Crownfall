package crownfall.model

import scala.util.{Failure, Success, Try}

sealed trait Result[+A]:
  def map[B](f: A => B): Result[B] =
    this match
      case Ok(value)  => Ok(f(value))
      case Err(error) => Err(error)

  def flatMap[B](f: A => Result[B]): Result[B] =
    this match
      case Ok(value)  => f(value)
      case Err(error) => Err(error)

  def filter(p: A => Boolean, error: GameError): Result[A] =
    this match
      case Ok(value)  => if p(value) then Ok(value) else Err(error)
      case Err(e) => Err(e)

case class Ok[+A](value: A) extends Result[A]
case class Err(error: GameError) extends Result[Nothing]

object Result:
  def fromOption[A](option: Option[A], error: GameError): Result[A] =
    option match
      case Some(value) => Ok(value)
      case None        => Err(error)

  def fromTry[A](attempt: Try[A], error: GameError): Result[A] =
    attempt match
      case Success(value) => Ok(value)
      case Failure(_)  => Err(error)