//kill some process with port:
//sudo kill -9 `sudo lsof -t -i:8080`
//run by curl: curl -D - localhost:8080
import $ivy.{
    `com.twitter::finagle-http:19.10.0`,
    `com.lihaoyi::requests:0.2.0`
}

import com.twitter.finagle.Service
import com.twitter.util.Future
val lengthService = Service.mk[String, Int] { req =>
Future.value(req.length)
}
lengthService("Hello TSUG").foreach(println)