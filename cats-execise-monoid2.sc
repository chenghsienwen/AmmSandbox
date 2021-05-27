import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.Monoid
import cats.instances.all._
import cats.syntax.semigroup._
import cats.syntax.option._

val c1 = Set(1, 2) |+| Set(2, 3)

val c2 = Set("A", "B") |+| Set("B", "C")

val c3 = Set(1.some, 2.some) |+| Set(2.some, 3.some)

println(s"c1: ${c1}, c2: ${c2}, c3: ${c3}")

