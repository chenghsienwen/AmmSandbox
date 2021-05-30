
import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.data.State
import State._   
import cats.instances.all._
import cats.syntax.applicative._
import scala.util.{ Failure, Success, Try }

type CalcState[A] = State[List[Int], A]
def evalOne(sym: String): CalcState[Int] = {
    Try(sym.toInt) match {
        case Success(v) => operand(v)
        case Failure(_) => sym match {
            case "+" => operator(_ + _)
            case "-" => operator(_ - _)
            case "*" => operator(_ * _)
            case "/" => operator(_ / _)
            case _ => 0.pure[CalcState]
        }
    }
}

def operand(num: Int): CalcState[Int] =
    State[List[Int], Int] { stack =>
        (num :: stack, num)
    }

def operator(func: (Int, Int) => Int): CalcState[Int] =
    State[List[Int], Int] {
        case b :: a :: tail =>
            val ans = func(a, b)
            (ans :: tail, ans)
        case _ =>
            sys.error("Fail!")
    }

def evalAll(input: List[String]): CalcState[Int] = {
    input.foldLeft(0.pure[CalcState])((a, b) => a.flatMap(_ => evalOne(b)))
}

val program = for {
    _ <- evalOne("1")
    _ <- evalOne("2")
    ans <- evalOne("+")
} yield ans
// program: cats.data.IndexedStateT[cats.Eval, List[Int], List[Int],Int] = cats.data.IndexedStateT@4449effe
program.runA(Nil).value
// res11: Int = 3

val multistageProgram = evalAll(List("1", "2", "+", "3", "*"))
// multistageProgram: CalcState[Int] = cats.data.IndexedStateT@7759956e
multistageProgram.runA(Nil).value
// res13: Int = 9

def evalInput(input: String): Int =
    evalAll(input.split(" ").toList).runA(Nil).value

val result = evalInput("1 2 + 3 4 + *")
// res15: Int = 21
println(result)



