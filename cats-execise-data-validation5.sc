import $ivy.`org.typelevel::cats-core:2.0.0`
interp.configureCompiler(_.settings.YpartialUnification.value = true)
@

import cats.Semigroup
import cats.data.Validated
import cats.data.Validated._ // for Valid and Invalid
import cats.instances.all._
import cats.syntax.semigroup._
import cats.syntax.apply._ // for mapN
import cats.syntax.validated._ // for valid and invalid
import cats.data.NonEmptyList

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
    def apply[E, A](f: A => Validated[E, A]): Predicate[E, A] =
        Pure(f)
    def lift[E, A](err: E, func: A => Boolean): Predicate[E, A] =
        Pure(a => if(func(a)) a.valid else err.invalid)
}
object Check {
    final case class Map[E, A, B, C](check: Check[E, A, B], func: B => C) extends Check[E, A, C] {
        def apply(a: A)(implicit s: Semigroup[E]):Validated[E, C] = check(a).map(func)
    }
    final case class Pure[E, A, B](func: A => Validated[E, B]) extends Check[E, A, B] {
        def apply(a: A)(implicit s: Semigroup[E]): Validated[E, B] = func(a)
    }

    final case class PurePredicate[E, A](pred: Predicate[E, A]) extends Check[E, A, A] {
        def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] = pred(a)
    }

    final case class FlatMap[E, A, B, C](check: Check[E, A, B], func: B => Check[E, A, C]) extends Check[E, A, C] {
        def apply(a: A)(implicit s: Semigroup[E]): Validated[E, C] = check(a).withEither(_.flatMap(b => func(b)(a).toEither))
    }

    final case class AndThen[E, A, B, C](check: Check[E, A, B], that: Check[E, B, C]) extends Check[E, A, C] {
        def apply(a: A)(implicit s: Semigroup[E]): Validated[E, C] =check(a).withEither(_.flatMap(b => that(b).toEither))
        //check(a).flatMap(b => that(b))
    }
    def apply[E, A](pred: Predicate[E, A]): Check[E, A, A] = PurePredicate(pred)

     def apply[E, A, B](func: A => Validated[E, B]): Check[E, A, B] = Pure(func)
}

type Errors = NonEmptyList[String]
def error(s: String): NonEmptyList[String] =
    NonEmptyList(s, Nil)
def longerThan(n: Int): Predicate[Errors, String] =
    Predicate.lift(error(s"Must be longer than $n characters"), str => str.size > n)
val alphanumeric: Predicate[Errors, String] =
    Predicate.lift(error(s"Must be all alphanumeric characters"), str => str.forall(_.isLetterOrDigit))
def contains(char: Char): Predicate[Errors, String] =
    Predicate.lift(error(s"Must contain the character $char"), str => str.contains(char))
def containsOnce(char: Char): Predicate[Errors, String] =
    Predicate.lift(error(s"Must contain the character $char only once"), str => str.filter(c => c == char).size == 1)

val checkUsername: Check[Errors, String, String] =
    Check(longerThan(3) and alphanumeric)

val splitEmail: Check[Errors, String, (String, String)] =
    Check(_.split('@') match {
        case Array(name, domain) =>
            (name, domain).validNel[String]
        case _ =>
            "Must contain a single @ character".
            invalidNel[(String, String)]
    })

val checkLeft: Check[Errors, String, String] =
    Check(longerThan(0))
val checkRight: Check[Errors, String, String] =
    Check(longerThan(3) and contains('.'))
val joinEmail: Check[Errors, (String, String), String] =
    Check { case (l, r) =>
        (checkLeft(l), checkRight(r)).mapN(_ + "@" + _)
    }
val checkEmail: Check[Errors, String, String] =
    splitEmail andThen joinEmail

final case class User(username: String, email: String)
def createUser(username: String, email: String): Validated[Errors, User] =
    (checkUsername(username), checkEmail(email)).mapN(User)

val v1 = createUser("Noel", "noel@underscore.io")
// res5: Validated[Errors, User] = Valid(User("Noel", "noel@underscore.io"))
val v2 = createUser("", "dave@underscore.io@io")
// res6: Validated[Errors, User] = Invalid(
//NonEmptyList(
// "Must be longer than 3 characters",
// List("Must contain a single @ character")

println(s"${v1}, ${v2}")