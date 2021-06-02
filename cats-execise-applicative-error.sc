import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.instances.all._
import cats.syntax.applicative._ // for pure
import cats.syntax.applicativeError._ // for raiseError etc
import cats.syntax.monadError._

// Alias Either to a type constructor with one parameter:
type ErrorOr[A] = Either[String, A]
// for ensure
val success = 42.pure[ErrorOr]
// success: ErrorOr[Int] = Right(42)
val failure = "Badness".raiseError[ErrorOr, Int]
// failure: ErrorOr[Int] = Left("Badness")
failure.handleErrorWith{
  case "Badness" =>
    256.pure[ErrorOr]
  case _ =>
    ("It's not ok").raiseError[ErrorOr, Int]
}
// res4: ErrorOr[Int] = Right(256)
success.ensure("Number to low!")(_ > 1000)
// res5: ErrorOr[Int] = Left("Number to low!")