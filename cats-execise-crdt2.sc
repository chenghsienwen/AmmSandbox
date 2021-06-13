import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.instances.all._
import cats.syntax.semigroup._ //for |+|
import cats.syntax.foldable._ // for combineAll
import cats.kernel.CommutativeMonoid

trait BoundedSemiLattice[A] extends CommutativeMonoid[A] {
    def combine(a1: A, a2: A): A
    def empty: A
}
object BoundedSemiLattice {
    implicit val intInstance: BoundedSemiLattice[Int] = new BoundedSemiLattice[Int] {
        def combine(a1: Int, a2: Int): Int = a1 max a2
        def empty:Int = 0
    }
    implicit def setInstance[A]: BoundedSemiLattice[Set[A]] = new BoundedSemiLattice[Set[A]] {
        def combine(a1: Set[A], a2: Set[A]): Set[A] = a1 union a2
        def empty: Set[A] = Set.empty[A] 
    }
}

final case class GCounter[A](counters: Map[String, A]) {
    def increment(machine: String, amount: A)(implicit m: CommutativeMonoid[A]): GCounter[A] = {
        GCounter(counters |+| Map(machine -> amount))
    }
    def merge(that: GCounter[A])(implicit m: BoundedSemiLattice[A]): GCounter[A] = {
        GCounter(this.counters |+| that.counters)
    }
    def total(implicit m: CommutativeMonoid[A]): A = counters.values.toList.combineAll
}

