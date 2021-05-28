

trait Printable[A] {
    def format(value: A): String
    def contramap[B](func: B => A): Printable[B] = new Printable[B] {
        def format(value: B): String = self.format(func(value))
    }
}    

def format[A](value: A)(implicit p: Printable[A]): String =
    p.format(value)

implicit val stringPrintable: Printable[String] = new Printable[String] {
    def format(value: String): String = value
}
implicit val booleanPrintable: Printable[Boolean] = new Printable[Boolean] {
    def format(value: Boolean): String =    
        if(value) "yes" else "no"
}
format("hello")
// res2: String = "'hello'"
format(true)
// res3: String = "yes"

final case class Box[A](value: A)

// implicit def boxPrintable[A](implicit p: Printable[A]): Printable[Box[A]] = p.contramap[Box[A]](_.value)

implicit def boxPrintable[A](implicit p: Printable[A]): Printable[Box[A]] = new Printable[Box[A]] {
    def format(box: Box[A]): String = p.format(box.value)
}
format(Box("hello world"))
// res4: String = "'hello world'"
format(Box(true))
// res5: String = "yes"

println(format(Box(true)))