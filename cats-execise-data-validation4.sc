import $ivy.`org.typelevel::cats-core:2.0.0`
interp.configureCompiler(_.settings.YpartialUnification.value = true)
@

import cats.Semigroup
import cats.data.Validated
import cats.data.Validated._ // for Valid and Invalid
import cats.instances.all._
import cats.syntax.semigroup._
import cats.syntax.apply._ // for mapN

sealed trait Predicate[E, A] {
    import Predicate._
    def and(that: Predicate[E, A]): Predicate[E, A] =
        And(this, that)
    def or(that: Predicate[E, A]): Predicate[E, A] =
        Or(this, that)
    def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] =
        this match {
            case Pure(func) =>
                func(a)
            case And(left, right) =>
                (left(a), right(a)).mapN((_, _) => a)
            case Or(left, right) =>
                left(a) match {
                    case Valid(_)  => Valid(a)
                    case Invalid(e1) =>  right(a) match {
                        case Valid(_) => Valid(a)
                        case Invalid(e2) => Invalid(e1 |+| e2)
                    }
        }
    }
}

sealed trait Check[E, A, B] {
    import Check._

    def apply(a: A)(implicit s: Semigroup[E]): Validated[E, B]
    def map[C](func: B => C): Check[E, A, C] = Map[E, A, B, C](this, func)
    def flatMap[C](func: B => Check[E, A, C]): Check[E, A, C] = FlatMap[E, A, B, C](this, func)
    def andThen[C](that: Check[E, B, C]): Check[E, A, C] = AndThen[E, A, B, C](this, that)
}

object Predicate {
    final case class And[E, A](left: Predicate[E, A], right: Predicate[E, A]) extends Predicate[E, A]
    final case class Or[E, A](left: Predicate[E, A], right: Predicate[E, A]) extends Predicate[E, A]
    final case class Pure[E, A](func: A => Validated[E, A]) extends Predicate[E, A]
}
object Check {
    final case class Map[E, A, B, C](check: Check[E, A, B], func: B => C) extends Check[E, A, C] {
        def apply(a: A)(implicit s: Semigroup[E]):Validated[E, C] = check(a).map(func)
    }
    final case class Pure[E, A](pred: Predicate[E, A]) extends Check[E, A, A] {
        def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] = pred(a)
    }

    final case class FlatMap[E, A, B, C](check: Check[E, A, B], func: B => Check[E, A, C]) extends Check[E, A, C] {
        def apply(a: A)(implicit s: Semigroup[E]): Validated[E, C] = check(a).withEither(_.flatMap(b => func(b)(a).toEither))
    }

    final case class AndThen[E, A, B, C](check: Check[E, A, B], that: Check[E, B, C]) extends Check[E, A, C] {
        def apply(a: A)(implicit s: Semigroup[E]): Validated[E, C] =check(a).withEither(_.flatMap(b => that(b).toEither))
        //check(a).flatMap(b => that(b))
    }
    def apply[E, A](pred: Predicate[E, A]): Check[E, A, A] = Pure(pred)
}
