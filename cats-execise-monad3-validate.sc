import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.MonadError
import cats.syntax.monadError._
import cats.syntax.applicativeError._
import cats.instances.all._
import scala.util.{Try, Success, Failure}

def validateAdult[F[_]](age: Int)(implicit me: MonadError[F, Throwable]): F[Int] = {
    age >= 18 match{
        case true => me.pure(age)
        case false => me.raiseError(throw new java.lang.IllegalArgumentException("Age must be greater than or equal to 18"))
    }
}

validateAdult[Try](18)
// res7: Try[Int] = Success(18)
// validateAdult[Try](8)
// res8: Try[Int] = Failure(
//
// java.lang.IllegalArgumentException: Age must be greater than or equal to 18
// )
type ExceptionOr[A] = Either[Throwable, A]
validateAdult[ExceptionOr](-1)
// res9: ExceptionOr[Int] = Left(
//
//java.lang.IllegalArgumentException: Age must be greater than or equal to 18
// )