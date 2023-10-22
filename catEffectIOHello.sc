import $ivy.`org.typelevel::cats-core:2.0.0`
import $ivy.`org.typelevel::cats-effect:2.0.0`
import $ivy.`io.catbird::catbird-effect:19.10.0`

import cats.implicits._
import cats.effect._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

  // Needed for `IO.sleep`
  implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)
  
  def program(args: List[String]): IO[Unit] =
    IO.sleep(1.second) *> IO(println(s"Hello world!. Args $args"))

  @main  
  def main(args: String*): Unit = {
    program(args.toList).unsafeRunSync()
  }
    
