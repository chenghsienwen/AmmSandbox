import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.Show
// import cats.Show
import cats.implicits._
// import cats.implicits._


case class Person(name: String, age: Int)
implicit val showPerson: Show[Person] = Show.show(person => person.name)

val john = Person("John", 31)
// john: Person = Person(John,31)

println(john.show)