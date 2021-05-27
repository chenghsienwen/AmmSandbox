import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.Eq
import cats.instances.all._
import cats.syntax.eq._

//should compile fail
// type mismatch;
//  found   : Int(1)
//  required: Option[Int]
// val res8 = List(1, 2, 3).map(Option(_)).filter(item => item === 1)
//                                                             ^
// Compilation Failed
List(1, 2, 3).map(Option(_)).filter(item => item === 1)
