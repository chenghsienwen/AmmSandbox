import $ivy.`org.typelevel::cats-core:2.0.0`
interp.configureCompiler(_.settings.YpartialUnification.value = true)
@

import cats.Semigroup
import cats.data.Validated
import cats.data.Validated._ // for Valid and Invalid
import cats.instances.all._
import cats.syntax.semigroup._
import cats.syntax.apply._ // for mapN


sealed trait Check[E, A] {
    import Check._
    def and(that: Check[E, A]): Check[E, A] = And(this, that)
    def or(that: Check[E, A]): Check[E, A] = Or(this, that)

    def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] = {
        this match {
            case Pure(func) => func(a)
            case And(left, right) => {
                // scalaOption:YpartialUnification is necessary
                //ref: https://stackoverflow.com/questions/49865936/scala-cats-validated-value-mapn-is-not-a-member-of-validatednel-tuple
               (left(a), right(a)).mapN((_, _) => a)
               //alternative
                // (left(a), right(a)) match {
                //     case (Valid(_), Valid(_)) => Valid(a)
                //     case (Valid(_), Invalid(e)) => Invalid(e)
                //     case (Invalid(e), Valid(_)) => Invalid(e)
                //     case (Invalid(e1), Invalid(e2)) => Invalid(e1 |+| e2) 
                // }
            }
            case Or(left, right) => {
                left(a) match {
                    case Valid(a) => Valid(a)
                    case Invalid(e1) => right(a) match {
                        case Valid(_) => Valid(a)
                        case Invalid(e2) => Invalid(e1 |+| e2) 
                    }
                }
            }
        }
    }
}

object Check {
    final case class Or[E, A](left: Check[E, A], right: Check[E, A]) extends Check[E, A]
    final case class And[E, A](left: Check[E, A], right: Check[E, A]) extends Check[E, A]
    final case class Pure[E, A](func: A => Validated[E, A]) extends Check [E, A]
    def pure[E, A](func: A => Validated[E, A]): Check[E, A] = Pure(func)
}

val a: Check[List[String], Int] =
    Check.pure { v =>
        if(v > 2) Valid(v)
        else Invalid(List("Must be > 2"))
    }
val b: Check[List[String], Int] =
    Check.pure { v =>
        if(v < -2) Valid(v)
        else Invalid(List("Must be < -2"))
    }
val check: Check[List[String], Int] =
    a and b

println(check(5))

println(check(0))