//kill some process with port:
//sudo kill -9 `sudo lsof -t -i:8080`
//run by curl: curl -D - localhost:8080
import $ivy.{
    `com.twitter::finagle-http:19.10.0`,
    `com.lihaoyi::requests:0.2.0`
}

import com.twitter.finagle.http.Method.Get
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.service.FailFastFactory.FailFast
import com.twitter.util.Future

val productServiceHttpClient: Service[Request, Response] =
Http.client.configured(FailFast(false)).newClient("127.0.0.1:8080",
"productService").toService
val response: Future[Response] =
productServiceHttpClient(Request(Get,"/products/22"))
response.foreach{ response =>
    println("hello")
    println(response.statusCode)
    println(response.contentString)
}