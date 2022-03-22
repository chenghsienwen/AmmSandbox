import coursierapi.{MavenRepository}

interp.load.ivy("com.thesamet.scalapb" %% "scalapb-json4s" % "0.10.1")

@
import scalapb.json4s.JsonFormat
import com.google.protobuf.any.{Any => PBAny}
import org.json4s.jackson.JsonMethods._

import scala.language.existentials
import java.nio.file.{Files, Paths}
import scala.io.Source

val path = "sampleMyJson.json"
val myJson = Source.fromFile(path).getLines.toList.mkString("\n")
// val result = JsonFormat.toJsonString(myProto)
val anyJson = parse(myJson)
println(s" json: $anyJson")