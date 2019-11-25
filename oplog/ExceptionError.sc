
import $ivy.`com.outr::perfolation:1.0.4`
import $ivy.`org.typelevel::mouse:0.23`
import $ivy.`org.typelevel::cats-effect:2.0.0`
import cats.Show
import mouse.option._
import perfolation._

object ExceptionError {
  implicit val exErrorShow: Show[Throwable] = Show.show(
    e => p"""{
            |     "exception": "${e.getClass.getCanonicalName}",
            |     "msg": "${Option(e.getMessage).cata(identity, "no message")}"
            |   }""".stripMargin
  )
}
