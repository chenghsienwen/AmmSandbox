object PipeOperator{
  implicit class Pipe[T](val v: T) extends AnyVal {
    def |>[U](f: T => U): U = f(v)

    // Additional suggestions:
    def $$[U](f: T => U): T =
      f(v).|>(_ => v)

    def #!(str: String = ""): T =
      println(s"$str:$v").|>(_ => v)
  }
}
