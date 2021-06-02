
import $ivy.`org.typelevel::cats-core:2.0.0`

import cats.Traverse
import cats .Id
import cats.Applicative
import cats.instances.all._
import cats.syntax.monad._
import cats.syntax.flatMap._ //for flatMap
import cats.syntax.functor._ //for map
import cats.syntax.traverse._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.Await

trait UptimeClient {
    def getUptime(hostname: String): Future[Int]
}

class UptimeService(client: UptimeClient) {
    def getTotalUptime(hostnames: List[String]): Future[Int] =
        hostnames.traverse(client.getUptime).map(_.sum)
}

class TestUptimeClient(hosts: Map[String, Int]) extends UptimeClient {
    def getUptime(hostname: String): Future[Int] =
        Future.successful(hosts.getOrElse(hostname, 0))
}

def testTotalUptime() = {
    val hosts = Map("host1" -> 10, "host2" -> 6)
    val client = new TestUptimeClient(hosts)
    val service = new UptimeService(client)
    val actual = Await.result(service.getTotalUptime(hosts.keys.toList), 5.second)
    val expected = hosts.values.sum
    assert(actual == expected)
}

 testTotalUptime()




