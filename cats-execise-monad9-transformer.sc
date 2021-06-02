//NOTE: only work on repl, script still fail
import $ivy.`org.typelevel::cats-core:2.0.0`
// interp.configureCompiler(_.settings.Ydelambdafy.value ="inline")
// @

import cats.Monad
import cats.instances.all._
import cats.syntax.monad._
import cats.syntax.flatMap._ //for flatMap
import cats.syntax.functor._ //for map
import cats.data.EitherT
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.Await
type Response[A] = EitherT[Future, String, A]

val powerLevels = Map(
    "Jazz" -> 6,
    "Bumblebee" -> 8,
    "Hot Rod" -> 10
)

def getPowerLevel(autobot: String): Response[Int] = {
    powerLevels.get(autobot) match {
        case Some(r) => EitherT.right(Future(r))
        case None => EitherT.left(Future("not found"))
    }
}

def canSpecialMove(ally1: String, ally2: String): Response[Boolean] = {
    for {
        r1 <- getPowerLevel(ally1)
        r2 <- getPowerLevel(ally2)
    } yield {
        r1 + r2 > 15
    }
}

def tacticalReport(ally1: String, ally2: String): String = {
    val stack = canSpecialMove(ally1, ally2).value
    Await.result(stack, 1.second) match {
        case Left(msg) =>
            s"Comms error: $msg"
        case Right(true) =>
            s"$ally1 and $ally2 are ready to roll out!"
        case Right(false) =>
            s"$ally1 and $ally2 need a recharge."
    }
}

tacticalReport("Jazz", "Bumblebee")
// res13: String = "Jazz and Bumblebee need a recharge."
tacticalReport("Bumblebee", "Hot Rod")
// res14: String = "Bumblebee and Hot Rod are ready to roll out!"
tacticalReport("Jazz", "Ironhide")
// res15: String = "Comms error: Ironhide unreachable"







