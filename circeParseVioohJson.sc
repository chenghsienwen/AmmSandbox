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

val path = "vioohResponseSample1.json"
val text = scala.io.Source.fromFile(path).getLines.toList.mkString("\n")
val json = parse(text).right.get

val nurlList = root.seatbid.each.bid.each.nurl.string.getAll(json)
println(s"nurl ${nurlList}")

// json.hcursor.downField("seatbid").focus.flatMap(_.asArray).map(_.map(j => j.hcursor.downField("bid").focus.flatMap(_.asArray).map(_.map(k => k.hcursor.get[Double]("price")))))
val price = root.seatbid.each.bid.each.price.double.getAll(json)

println(s"price: ${price}")