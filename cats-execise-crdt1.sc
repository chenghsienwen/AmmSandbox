import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.instances.all._
import cats.syntax.semigroup._ //for |+|

final case class GCounter(counters: Map[String, Int]) {
    def increment(machine: String, amount: Int): GCounter = {
        GCounter(counters |+| Map(machine -> amount))
    }
    def merge(that: GCounter): GCounter = {
        GCounter(counters ++ that.counters.map{ i =>
            i._1 -> (i._2 max counters.get(i._1).getOrElse(0))
        })
    }
    def total: Int = counters.values.sum
}