import coursierapi.{MavenRepository}

val circeVersion = "0.10.0"

interp.load.ivy( "io.circe" %% "circe-core" % circeVersion)
interp.load.ivy("io.circe" %% "circe-generic" % circeVersion)
interp.load.ivy("io.circe" %% "circe-parser" % circeVersion)
interp.load.ivy("io.circe" %% "circe-optics" % circeVersion)
@
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import io.circe.{Json, JsonObject}
import io.circe.optics.JsonPath._

case class UserHit(id: Int, username: String, nickname: String, hit: Int)

val path = "Simple2.json"
val text = scala.io.Source.fromFile(path).getLines.toList.mkString("\n")
val json = parse(text).right.get

val payload =json.hcursor.downField("payload").focus
println(s"payload: ${payload}")
val user = payload.flatMap(i => decode[UserHit](i.spaces2).toOption)
println(s"user ${user}")
