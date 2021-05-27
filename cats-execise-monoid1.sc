
trait Semigroup[A] {
    def combine(x: A, y: A): A
}
trait Monoid[A] extends Semigroup[A] {
    def empty: A
}
object Monoid {
    def apply[A](implicit monoid: Monoid[A]) = monoid
    def combine[A](x: A, y: A)(implicit monoid: Monoid[A]) = monoid.combine(x, y)
    def empty[A](implicit monoid: Monoid[A]) = monoid.empty
}

object MonoidInstances {
    implicit val booleanAndMono = new Monoid[Boolean] {
        def combine(x: Boolean, y: Boolean): Boolean = x && y
        def empty = true
    }

    implicit val booleanOrMono = new Monoid[Boolean] {
        def combine(x: Boolean, y: Boolean): Boolean = x || y
        def empty = false
    }

    implicit val booleanEitherMono = new Monoid[Boolean] {
        def combine(x: Boolean, y: Boolean): Boolean = (x && !y) || (!x && y)
        def empty = false
    }

    implicit val booleanXnorMono = new Monoid[Boolean] {
        def combine(x: Boolean, y: Boolean): Boolean = (x || !y) && (!x || y)
        def empty = true
    }

    implicit def setMono[T]: Monoid[Set[T]] = new Monoid[Set[T]] {
        def combine(x: Set[T], y: Set[T]): Set[T] = x union y
        def empty = Set.empty[T]
    }
}

import MonoidInstances._

val intSetMonoid = Monoid[Set[Int]]
val strSetMonoid = Monoid[Set[String]]

val c1 = Monoid.combine(Set(1, 2), Set(2, 3))

val c2 = Monoid.combine(Set("A", "B"), Set("B", "C"))

println(s"c1: ${c1}, c2: ${c2}")

