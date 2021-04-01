import $ivy.`com.propensive::magnolia:0.17.0`

// import language.experimental.macros, magnolia._
// import language.experimental.macros
// import probably._
// import contextual.data.scalac._
// import contextual.data.fqt._
// import contextual.data.txt._
import $file.magnolia.show
import language.experimental.macros, magnolia._

sealed trait Tree[+T]
case class Branch[+T](left: Tree[T], right: Tree[T]) extends Tree[T]
case class Leaf[+T](value: T) extends Tree[T]


val result = implicitly[show.Show[String, Branch[String]]].show(Branch(Leaf("LHS"), Leaf("RHS")))

println(result)
