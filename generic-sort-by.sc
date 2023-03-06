

case class User(name: String = "", age: Int = 0, value: Double = 0.0)
val useStreams: String => String = { spacedString =>
    val first = spacedString.take(1).toLowerCase
    val rest =
      spacedString.toStream.sliding(2).foldLeft("") { (str, charStream) =>
        val added = charStream.toList match {
          case ' ' :: ' ' :: _ => ""
          case ' ' :: '_' :: _ => ""
          case '_' :: ' ' :: _ => ""
          case '_' :: '_' :: _ => ""
          case ' ' :: other    => other.mkString.toUpperCase
          case '_' :: other    => other.mkString.toUpperCase
          case _ :: other :: _ if other != ' ' && other != '_' =>
            other.toLower.toString
          case _ => ""
        }
        str + added
      }
    first + rest
  }

  implicit class StringCamel(str: String) {
    def toCamelCase: String = {
      useStreams(str)
    }
  }

def getSorted(order: String, desc: Int, data: Vector[User]): Vector[User] = {
    val fields = User().getClass.getDeclaredFields.map(_.getName)
    val index = fields.indexOf(order.toCamelCase)
    val element = data.headOption.map(_.productElement(index)).getOrElse("")
    (desc) match {
      case (0) =>
        element match {
          case _: String => data.sortBy(_.productElement(index).asInstanceOf[String])
          case _: Double => data.sortBy(_.productElement(index).asInstanceOf[Double])
          case _: Long => data.sortBy(_.productElement(index).asInstanceOf[Long])
          case _: Int => data.sortBy(_.productElement(index).asInstanceOf[Int])
          case _ => data.sortBy(_.productElement(index).asInstanceOf[String])
        }
      case _ => {
        element match {
          case _: String => data.sortBy(_.productElement(index).asInstanceOf[String])(Ordering[String].reverse)
          case _: Double => data.sortBy(_.productElement(index).asInstanceOf[Double])(Ordering[Double].reverse)
          case _: Long => data.sortBy(_.productElement(index).asInstanceOf[Long])(Ordering[Long].reverse)
          case _: Int => data.sortBy(_.productElement(index).asInstanceOf[Int])(Ordering[Int].reverse)
          case _ => data.sortBy(_.productElement(index).asInstanceOf[String])(Ordering[String].reverse)
        }
      }
    }
  }

val list = Vector(User("a", 0, 0.1), User("b", 1, 0.2), User("c", 2, 0.3), User("d", 3, 0.4))

println(getSorted("name", 0, list))

println(getSorted("name", 1, list))

println(getSorted("age", 0, list))

println(getSorted("age", 1, list))
