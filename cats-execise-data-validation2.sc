import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.Semigroup
import cats.instances.all._
import cats.syntax.either._
import cats.syntax.semigroup._

sealed trait Check[E, A] {
    import Check._
    def and(that: Check[E, A]): Check[E, A] = {
        And(this, that)
    }
    def apply(a: A)(implicit s: Semigroup[E]): Either[E, A] = {
        this match {
            case Pure(func) => func(a)
            case And(left, right) => {
                (left(a), right(a)) match {
                    case (Left(e1), Left(e2)) => (e1 |+| e2).asLeft
                    case (Right(_), Left(e)) => e.asLeft
                    case (Left(e), Right(_)) => e.asLeft
                    case (Right(_), Right(_)) => a.asRight
                }
            }
        }
    }
}

object Check {
    final case class And[E, A](left: Check[E, A], right: Check[E, A]) extends Check[E, A]
    final case class Pure[E, A](func: A => Either[E, A]) extends Check [E, A]
    def pure[E, A](func: A => Either[E, A]): Check[E, A] = Pure(func)
}

// final case class CheckF[E, A](func: A => Either[E, A]) {
//     def apply(a: A): Either[E, A] = func(a)

//     def and(that: CheckF[E, A])(implicit s: Semigroup[E]): CheckF[E, A] = {
//         CheckF { a =>
//             (this(a), that(a)) match {
//                 case (Left(e1), Left(e2)) => (e1 |+| e2).asLeft
//                 case (Right(_), Left(e)) => e.asLeft
//                 case (Left(e), Right(_)) => e.asLeft
//                 case (Right(_), Right(_)) => a.asRight
//             }
//         }
//     }
// }

val a: Check[List[String], Int] =
    Check.pure { v =>
        if(v > 2) v.asRight
        else List("Must be > 2").asLeft
    }
val b: Check[List[String], Int] =
    Check.pure { v =>
        if(v < -2) v.asRight
        else List("Must be < -2").asLeft
    }
val check: Check[List[String], Int] =
    a and b

println(check(5))

println(check(0))