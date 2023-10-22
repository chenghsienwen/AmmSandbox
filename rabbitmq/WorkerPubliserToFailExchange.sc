import $ivy.{
  `com.twitter::finagle-http:19.10.0`,
  `com.newmotion::akka-rabbitmq:5.0.1`
}
import akka.actor.{ActorRef, ActorSystem}
import com.twitter.util.Future
import com.newmotion.akka.rabbitmq._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import com.newmotion.akka.rabbitmq.ChannelActor.GetState
/*
* use scala version = 2.12
* how to install:
* val sh: Nothing = null -(c) '(echo "#!/usr/bin/env sh" && curl -L https://github.com/lihaoyi/Ammonite/releases/download/2.0.1/2.12-2.0.1) > /usr/local/bin/amm && chmod +x /usr/local/bin/amm' && amm
* reference: https://ammonite.io/#OlderScalaVersions
* */

implicit val system = ActorSystem()
val factory         = new ConnectionFactory()
factory.setHost("dsp-docker02.vpon.com")
factory.setPort(5672)
factory.setUsername("admin")
factory.setPassword("ji3g4wu0h96")
val connection         = system.actorOf(ConnectionActor.props(factory), "akka-rabbitmq")
val exchange           = "segment.fail"

val settings = new java.util.HashMap[String, Object]
connection ! CreateChannel(ChannelActor.props(setupPublisher), Some("fail-publisher"))
Thread.sleep(5000)
run()
Thread.sleep(5000)
def setupPublisher(channel: Channel, self: ActorRef) {
  val queue = channel.queueDeclare("segment_fail_queue", true, false, false, settings).getQueue
  channel.queueBind(queue, exchange, "segment_fail_queue")
}




def fromBytes(x: Array[Byte]) = new String(x, "UTF-8")

def toBytes(x: Long) = x.toString.getBytes("UTF-8")


def run (): Unit = {
  val publisher = system.actorSelection("/user/akka-rabbitmq/fail-publisher")
  def publish(channel: Channel, index: Long) {
    channel.basicPublish(exchange, "segment_fail_queue", null, toBytes(index))
  }
  val msgs = 0L to 30L
  msgs.foreach { x =>
    publisher ! ChannelMessage(publish(_, x), dropIfNoChannel =true)

    println("request message:" + x + " ")
  }
}

