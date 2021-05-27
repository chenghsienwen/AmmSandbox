//ref:https://typelevel.org/cats/typeclasses/show.html

import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.Show
import cats.syntax.show._


case class Person(name: String, age: Int)
implicit val showPerson: Show[Person] = Show.show(person => s"name: ${person.name}, age: ${person.age}")

println(Person("John", 31).show)