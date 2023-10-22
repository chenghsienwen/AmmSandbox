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

val path = "dableResponseSample2.json"
val text = scala.io.Source.fromFile(path).getLines.toList.mkString("\n")
val json = parse(text).right.get

val admList = root.seatbid.each.bid.each.adm.string.getAll(json)
println(s"admList ${admList}")
val trackers = admList.flatMap{a => 
  val item = parse(a).right.get
  root.native.eventtrackers.each.url.string.getAll(item)
}
println(s"result ${trackers}")