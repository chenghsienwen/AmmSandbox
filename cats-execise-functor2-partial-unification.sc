import $ivy.`org.typelevel::cats-core:2.0.0`
//ref: https://gist.github.com/MateuszKubuszok/a80503b28f289f08f2f6c6c70871e8d3#log-implicits
interp.configureCompiler(_.settings.YpartialUnification.value = true)
@
// if no partialUnification in scala 2.12, it would show following error
// value map is not a member of Int => Double
// val func3 = func1 map func2
//                   ^
// Compilation Failed
import cats.Functor
import cats.instances.function._ // for Functor
import cats.syntax.functor._ // for map


val func1 = (x: Int) => x.toDouble
val func2 = (y: Double) => y * 2
val func3 = func1 map func2

println(func3(3))