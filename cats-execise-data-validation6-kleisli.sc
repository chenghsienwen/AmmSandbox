import $ivy.`org.typelevel::cats-core:2.0.0`
interp.configureCompiler(_.settings.YpartialUnification.value = true)
@

import cats.data.{Kleisli, NonEmptyList}
import cats.Semigroup
import cats.data.Validated
import cats.data.Validated._ // for Valid and Invalid
import cats.instances.all._
import cats.syntax.semigroup._ //for |+|
import cats.syntax.apply._ // for mapN
import cats.syntax.either._ // for asRight and asLeft
import cats.syntax.validated._ // for valid and invalid

type Errors = NonEmptyList[String]
def error(s: String): NonEmptyList[String] =
    NonEmptyList(s, Nil)

type Result[A] = Either[Errors, A]
type Check[A, B] = Kleisli[Result, A, B]

def check[A, B](func: A => Result[B]): Check[A, B] =
    Kleisli(func)
def checkPred[A](pred: Predicate[Errors, A]): Check[A, A] =
    Kleisli[Result, A, A](pred.run)

sealed trait Predicate[E, A] {
    import Predicate._
    def run(implicit s: Semigroup[E]): A => Either[E, A] =
        (a: A) => this(a).toEither
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

object Predicate {
    final case class And[E, A](left: Predicate[E, A], right: Predicate[E, A]) extends Predicate[E, A]
    final case class Or[E, A](left: Predicate[E, A], right: Predicate[E, A]) extends Predicate[E, A]
    final case class Pure[E, A](func: A => Validated[E, A]) extends Predicate[E, A]
    def apply[E, A](f: A => Validated[E, A]): Predicate[E, A] =
        Pure(f)
    def lift[E, A](err: E, func: A => Boolean): Predicate[E, A] =
        Pure(a => if(func(a)) a.valid else err.invalid)
}

def longerThan(n: Int): Predicate[Errors, String] =
    Predicate.lift(error(s"Must be longer than $n characters"), str => str.size > n)
val alphanumeric: Predicate[Errors, String] =
    Predicate.lift(error(s"Must be all alphanumeric characters"), str => str.forall(_.isLetterOrDigit))
def contains(char: Char): Predicate[Errors, String] =
    Predicate.lift(error(s"Must contain the character $char"), str => str.contains(char))
def containsOnce(char: Char): Predicate[Errors, String] =
    Predicate.lift(error(s"Must contain the character $char only once"), str => str.filter(c => c == char).size == 1)

val checkUsername: Check[String, String] =
    checkPred(longerThan(3) and alphanumeric)

val splitEmail: Check[String, (String, String)] =
    check(_.split('@') match {
        case Array(name, domain) =>
            Right((name, domain))
        case _ =>
            Left(error("Must contain a single @ character"))
    })

val checkLeft: Check[String, String] =
    checkPred(longerThan(0))
val checkRight: Check[String, String] =
    checkPred(longerThan(3) and contains('.'))
val joinEmail: Check[(String, String), String] =
    check { case (l, r) =>
        (checkLeft(l), checkRight(r)).mapN(_ + "@" + _)
    }
val checkEmail: Check[String, String] =
    splitEmail andThen joinEmail

final case class User(username: String, email: String)
def createUser(username: String, email: String): Either[Errors, User] =
    (checkUsername(username), checkEmail(email)).mapN(User)

val v1 = createUser("Noel", "noel@underscore.io")
// res2: Either[Errors, User] = Right(User("Noel", "noel@underscore.io"))
val v2 = createUser("", "dave@underscore.io@io")
// res3: Either[Errors, User] = Left(
//NonEmptyList("Must be longer than 3 characters", List())
// )

println(s"${v1}, ${v2}")