import $ivy.`com.typesafe.akka::akka-http:10.1.1`
import $ivy.`com.typesafe.akka::akka-stream:2.5.12`
import akka.http.scaladsl._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{HttpEntity, ContentTypes}

implicit val system = akka.actor.ActorSystem("MySystem")
implicit val materializer = akka.stream.ActorMaterializer()
implicit val executionContext = system.dispatcher

  val route =
  path("hello") {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
    }
  }
val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)

def shutdown = bindingFuture.flatMap(_.unbind()).onComplete{_ => system.terminate() }

/*
 Remember that everything is done asynchronously, so don't exit...
 Following lines allow to wait for the actor system to be terminated
 */
import scala.concurrent.Await
import scala.concurrent.duration._
Await.result(system.whenTerminated, 10.seconds)
@main
def main() = {
  // Await.ready(system.whenTerminated, Duration.Inf)
  println("server started at 8080")
  println("akka http: " + requests.get("http://localhost:8080").text())
}