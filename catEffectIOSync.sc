import $ivy.`org.typelevel::cats-core:2.0.0`
import $ivy.`org.typelevel::cats-effect:2.0.0`
import $ivy.`org.typelevel::cats-effect-laws:2.0.0`
import $ivy.`io.catbird::catbird-effect:19.10.0`

import cats.implicits._
import cats.effect._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import cats.effect.ExitCode

import cats.effect.{IO, Sync}
import cats.laws._

val F = Sync[IO]

lazy val stackSafetyOnRepeatedRightBinds = {
  val result = (0 until 10000).foldRight(F.delay(())) { (_, acc) =>
    F.delay(()).flatMap(_ => acc)
  }

  result <-> F.pure(())
}

  val ioa = F.delay(println("Hello world!"))

ioa.unsafeRunSync()  
