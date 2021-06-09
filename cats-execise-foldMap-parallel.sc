import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.{Monoid, Monad}
import cats.instances.all._
import cats.syntax.foldable._
import cats.syntax.traverse._

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
/** single thread map-reduce function.
* Maps `func` over `values` and reduces using a `Monoid[B]`.
*/
def foldMap[A, B: Monoid](values: Vector[A])(func: A => B): B = {
    values.map(func).combineAll
}

foldMap(Vector(1, 2, 3))(identity)

foldMap(Vector(1, 2, 3))(_.toString + "! ")

foldMap("Hello world!".toVector)(_.toString.toUpperCase)

/**
 * Parallelising foldMap
*/
def parallelFoldMap[A, B : Monoid](values: Vector[A])(func: A => B): Future[B] = {
    val processorCount = Runtime.getRuntime.availableProcessors
    val batchCount = 1 + (values.size / processorCount)
    val futureList = (values.grouped(batchCount).map{ batch =>
        Monad[Future].pure(batch).map{ task =>
            foldMap(task)(func)
        }
    }).toList.sequence
    futureList.map(i => i.combineAll)
}

parallelFoldMap(Vector(1, 2, 3))(identity)

parallelFoldMap(Vector(1, 2, 3))(_.toString + "! ")

parallelFoldMap("Hello world!".toVector)(_.toString.toUpperCase)


