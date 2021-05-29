
//NOTE: only work on repl, script still fail
import $ivy.`org.typelevel::cats-core:2.0.0`
//ref: https://github.com/com-lihaoyi/Ammonite/issues/534
//ref: https://gist.github.com/MateuszKubuszok/a80503b28f289f08f2f6c6c70871e8d3#log-implicits
//ref: https://www.scala-lang.org/api/2.12.4/scala-compiler/scala/tools/nsc/Settings.html
interp.configureCompiler(_.settings.Ydelambdafy.value ="inline")
@

import cats.data.Writer
import cats.instances.all._
import cats.syntax.writer._
import cats.syntax.applicative._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration._
import scala.concurrent.Await

def slowly[A](body: => A) = 
    try body finally Thread.sleep(100)

def factorial(n: Int): Int = {
    val ans = slowly(if(n == 0) 1 else n * factorial(n - 1))
    println(s"fact $n $ans")
    ans
}
type Logged[A] = Writer[Vector[String], A]

def factorialW(n: Int): Logged[Int] =
    for {
        ans <- if(n == 0) {
                        1.pure[Logged]
                    } else {
                        slowly(factorialW(n - 1).map(_ * n))
                    }
        _ <- Vector(s"fact $n $ans").tell
    } yield ans

val res = Await.result(Future.sequence(Vector(
    Future(factorialW(5).run),
    Future(factorialW(5).run)
)), 5.seconds)
println(res)

