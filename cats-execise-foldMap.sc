import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.Monoid
import cats.instances.all._
import cats.syntax.foldable._
/** Single-threaded map-reduce function.
* Maps `func` over `values` and reduces using a `Monoid[B]`.
*/
def foldMap[A, B: Monoid](values: Vector[A])(func: A => B): B = {
    values.map(func).combineAll
}