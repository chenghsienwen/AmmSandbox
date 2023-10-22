import $ivy.`com.typesafe.akka::akka-http:10.1.7`
import $ivy.`com.typesafe.akka::akka-stream:2.5.21`
import $ivy.`com.lihaoyi::requests:0.2.0`
import akka.http.scaladsl._
import akka.http.scaladsl.server.Directives._
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object StaticServer {
  implicit val system = akka.actor.ActorSystem("MySystem")
  implicit val materializer = akka.stream.ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val routes = pathPrefix("browse") { getFromBrowseableDirectory(".") }
  Http().bindAndHandle(routes, "0.0.0.0", 8080).andThen{case _ => println("Ready.")}
  
}


@main
def main() = {
  Await.ready(StaticServer.system.whenTerminated, Duration.Inf)
  println("server started at 8080")
  println("akka http: " + requests.get("http://localhost:8080").text())
}