
import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.Monad
import cats.instances.all._
import cats.syntax.monad._
import cats.syntax.flatMap._ //for flatMap
import cats.syntax.functor._ //for map
import scala.annotation.tailrec

sealed trait Tree[+A]
final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

implicit val treeMonad: Monad[Tree] = new Monad[Tree] {
    def pure[A](a: A): Tree[A] = Leaf(a)
    def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] = {
        fa match {
            case Branch(left, right) => Branch(flatMap(left)(f), flatMap(right)(f))
            case Leaf(value) => f(value)
        }
    }

    // @tailrec 
    //not stack safe
    def tailRecM[A, B](a: A)(fn: A => Tree[Either[A, B]]): Tree[B] = {
        flatMap(fn(a)) {
            case Left(value) => tailRecM(value)(fn)
            case Right(value) => Leaf(value)
        }
    }
}
object Tree {
    def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
        Branch(left, right)
    def leaf[A](value: A): Tree[A] =
        Leaf(value)
}

val result1 = Tree.branch(Tree.leaf(100), Tree.leaf(200)).flatMap(x => Tree.branch(Tree.leaf(x - 1), Tree.leaf(x + 1)))

val result2 = for {
    a <- Tree.branch(Tree.leaf(100), Tree.leaf(200))
    b <- Tree.branch(Tree.leaf(a - 10), Tree.leaf(a + 10))
    c <- Tree.branch(Tree.leaf(b - 1), Tree.leaf(b + 1))
} yield c
// res6: Tree[Int] = Branch(
//Branch(Branch(Leaf(89), Leaf(91)), Branch(Leaf(109), Leaf(111))),
//Branch(Branch(Leaf(189), Leaf(191)), Branch(Leaf(209), Leaf(211))
//)
// )

println(s"$result1, \n$result2")





