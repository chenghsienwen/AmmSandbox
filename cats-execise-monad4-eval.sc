import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.Eval

// def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
//     as match {
//         case head :: tail =>
//             fn(head, foldRight(tail, acc)(fn))
//         case Nil =>
//             acc
// }
//would cause stack overflow

def foldRightEval[A, B](as: List[A], acc: Eval[B])(fn: (A, Eval[B]) => Eval[B]): Eval[B] = {
    as match {
        case head :: tail =>
            Eval.defer(fn(head, foldRightEval(tail, acc)(fn)))
        case Nil =>
            acc
    }
}

def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
    foldRightEval(as, Eval.now(acc)) { (a, b) =>
        b.map(fn(a, _))
    }.value

 val result = foldRight((1 to 100000).toList, 0L)(_ + _)
// res24: Long = 5000050000L

//  val result = foldRight((1 to 10000000).toList, 0L)(_ + _)
 //still cause java.lang.OutOfMemoryError: Java heap space

println(result)

