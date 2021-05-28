interp.load.ivy("org.typelevel" %% "cats-core" % "2.1.1")

@
import cats.Monad
import cats.syntax.functor._ // for map
import cats.syntax.flatMap._ // for flatMap
import cats.instances.all._
import cats.Id
def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
for {
    x <- a
    y <- b
} yield x*x + y*y

sumSquare(Option(3), Option(4))
// res7: Option[Int] = Some(25)
sumSquare(List(1, 2, 3), List(4, 5))
// res8: List[Int] = List(17, 26, 20, 29, 25, 34)

val r1 = sumSquare(3:Id[Int], 4:Id[Int])
//25
println(r1)

