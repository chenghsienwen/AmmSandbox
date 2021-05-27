import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.Monoid
import cats.instances.all._
import cats.syntax.semigroup._
import cats.syntax.option._

implicit val orderMono = new Monoid[Order] {
    def combine(x: Order, y: Order): Order = x.copy(totalCost = x.totalCost |+| y.totalCost, quantity = x.quantity |+| y.quantity)
    def empty: Order = Order(0, 0)
}

def addInt(items: List[Int]): Int = items.foldLeft(Monoid[Int].empty)((a, b) => a |+| b)

def add[A](items: List[A])(implicit monoid: Monoid[A]): A = items.foldLeft(monoid.empty)((a, b) => a |+| b)

case class Order(totalCost: Double, quantity: Double)

val list1 = List(Some(1), Some(2), None)

// List(Some(1), Some(2)) would not compile: could not find implicit value for parameter monoid: cats.Monoid[Some[Int]]
val list2 = (0 to 5).map(i => Order(i, i)).toList
val result1 = add(list1)
val result2 = add(list2)

println(s"result1: ${result1}, result2: ${result2}")