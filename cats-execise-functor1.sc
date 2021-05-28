import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.Functor
import cats.instances.all._
import cats.syntax.functor._


// trait Functor[F[_]] {
//   def map[A, B](fa: F[A])(f: A => B): F[B]
// }

sealed trait Tree[+A]
final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = {
        fa match {
            case Branch(left, right) => Branch(map(left)(f), map(right)(f))
            case Leaf(value) => Leaf(f(value))
        }
    }
}

object Tree {
    def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
        Branch(left, right)
    def leaf[A](value: A): Tree[A] =
        Leaf(value)
}

val result = Tree.branch(Tree.leaf(10), Tree.leaf(20)).map(_ * 2)

println(result)