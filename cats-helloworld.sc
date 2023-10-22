interp.load.ivy("org.typelevel" %% "cats-core" % "2.1.1")

@
import cats.implicits._

case class Tier(index: Int)
Option(Tier(0)) |+| Option(Tier(1))