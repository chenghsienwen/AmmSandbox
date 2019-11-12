//kill some process with port:
//sudo kill -9 `sudo lsof -t -i:8080`
//run by curl: curl -D - localhost:8080
import $ivy.{
    `com.twitter::finagle-http:19.10.0`,
    `com.lihaoyi::requests:0.2.0`
}

import com.twitter.finagle.{Filter, Service}
import com.twitter.util.Future
val someStringMetrics = Service.mk[String,Int] { req =>
    println("req :"+ req)
    Future.value(req.length)
}
val evenMetricsFilter = new Filter[Float, Boolean, String, Int] {
    override def apply(input: Float,
    stringMetricsService: Service[String, Int]): Future[Boolean] = {

        stringMetricsService(input.toString).map(_ % 2 == 0)
    }
}
val result = (evenMetricsFilter andThen someStringMetrics)(10)

println("result:" + result)