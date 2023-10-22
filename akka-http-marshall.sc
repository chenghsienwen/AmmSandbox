import $ivy.`com.typesafe.akka::akka-http:10.1.7`
import $ivy.`com.typesafe.akka::akka-stream:2.5.21`
import $ivy.`com.lihaoyi::requests:0.2.0`
import $ivy.`org.scalatest::scalatest:3.0.4`
import akka.http.scaladsl._
import akka.http.scaladsl.server.Directives._
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._

implicit val system = akka.actor.ActorSystem("MySystem")
implicit val materializer = akka.stream.ActorMaterializer()
implicit val executionContext = system.dispatcher
import org.scalatest._

val string = "Yeah"
val entityFuture = Marshal(string).to[MessageEntity]
val entity = Await.result(entityFuture, 1.second) // don't block in non-test code!
entity.contentType shouldEqual ContentTypes.`text/plain(UTF-8)`

val errorMsg = "Easy, pal!"
val responseFuture = Marshal(420 -> errorMsg).to[HttpResponse]
val response = Await.result(responseFuture, 1.second) // don't block in non-test code!
response.status shouldEqual StatusCodes.EnhanceYourCalm
response.entity.contentType shouldEqual ContentTypes.`text/plain(UTF-8)`

val request = HttpRequest(headers = List(headers.Accept(MediaTypes.`application/json`)))
val responseText = "Plaintext"
val respFuture = Marshal(responseText).toResponseFor(request) // with content negotiation!
a[Marshal.UnacceptableResponseContentTypeException] should be thrownBy {
  Await.result(respFuture, 1.second) // client requested JSON, we only have text/plain!
}