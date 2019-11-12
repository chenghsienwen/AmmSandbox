//kill some process with port:
//sudo kill -9 `sudo lsof -t -i:8080`
//run by curl: curl -D - localhost:8080
import $ivy.{
    `com.twitter::finagle-http:19.10.0`,
    `com.lihaoyi::requests:0.2.0`
}

import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http
import com.twitter.util.{Await, Future}


val service = new Service[http.Request, http.Response] {
def apply(req: http.Request): Future[http.Response] =
    Future.value(
    http.Response(req.version, http.Status.Ok)
    )
}
val server = Http.serve(":8080", service)

try {
    println("hello finagle: " + requests.get("http://localhost:8080"))
} finally {
    server.close()
}