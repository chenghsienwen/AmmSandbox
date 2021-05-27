final case class Cat(name: String, age: Int, color: String)

trait Printable[A] {
   def format(a: A): String
}



object PrintableInstance {
    implicit val strPrint: Printable[String] = new Printable[String] {
        def format(a: String): String = a
    }
    implicit val intPrint: Printable[Int]  = new Printable[Int] {
        def format(a: Int): String = a.toString
    }
}

object Printable {
    def format[A](a: A)(implicit p: Printable[A]): String = p.format(a)   
}
object PrintableSyntax {
    implicit class PrintableOps[A](a: A)(implicit p: Printable[A]) {
        def show: String = p.format(a)
    }
}

import PrintableSyntax._
import PrintableInstance._
implicit val catPrint: Printable[Cat] = new Printable[Cat] {
    def format(cat: Cat): String = {
        val name = Printable.format(cat.name)
        val age = Printable.format(cat.age)
        val color = Printable.format(cat.color)
        s"$name is a $age year-old $color cat."
    }
}

println(Printable.format("xxx"))
println("aaa".show)
println(Cat("meme", 5, "yellow").show)