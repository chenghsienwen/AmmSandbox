import ammonite.ops._

import $ivy.`com.typesafe:config:1.3.1`
import $ivy.`com.typesafe.akka:akka-http_2.12:10.0.6`
import $ivy.`com.lihaoyi::requests:0.2.0`
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.typesafe.config.ConfigFactory
import java.io._

@main
def main() = {
  val fileConfig = ConfigFactory.parseFile(new File("resources/application.conf"))
  val config = ConfigFactory.load(fileConfig)

  println(config)

  implicit val actorSystem = ActorSystem("system", config)
  implicit val actorMaterializer = ActorMaterializer()

  val route =
    pathSingleSlash {
      get {
        complete {
          "Hello world"
        }
      }
    }
  Http().bindAndHandle(route,"localhost",8080)
  println("server started at 8080")
  println("akka http: " + requests.get("http://localhost:8080").text())
}