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
val exchange           = "segment.work"
val deadLetterExchange = "segment.retry"

val settings = new java.util.HashMap[String, Object]
settings.put("x-dead-letter-exchange", deadLetterExchange)
//  settings.put("x-message-ttl", 0.asInstanceOf[Object])
connection ! CreateChannel(ChannelActor.props(setupSubscriber), Some("subscriber1"))
//  connection ! CreateChannel(ChannelActor.props(setupSubscriber2), Some("subscriber2"))
Thread.sleep(5000)
run()
Thread.sleep(5000)



def setupSubscriber(channel: Channel, self: ActorRef) {
  val queue = channel.queueDeclare("segment_work_queue_test", true, false, false, settings).getQueue
  channel.queueBind(queue, exchange, "segment_work_queue_test")
  val consumer = new DefaultConsumer(channel) {
    self.path.name
    override def handleDelivery(
                                 consumerTag: String,
                                 envelope: Envelope,
                                 properties: BasicProperties,
                                 body: Array[Byte]
                               ) {
       println(self.path.name + " received: " + fromBytes(body))
     channel.basicNack(envelope.getDeliveryTag, false, false)
    }
  }
  channel.basicConsume(queue, false, consumer)
}

// def setupSubscriber2(channel: Channel, self: ActorRef) {
//   val queue = channel.queueDeclare("segment_retry_queue_test", true, false, false, null).getQueue
//   channel.queueBind(queue, deadLetterExchange, "segment_retry_queue_test")
//   val consumer = new DefaultConsumer(channel) {
//     self.path.name
//     override def handleDelivery(
//                                  consumerTag: String,
//                                  envelope: Envelope,
//                                  properties: BasicProperties,
//                                  body: Array[Byte]
//                                ) {
//       println(self.path.name + " received: " + fromBytes(body))
//              channel.basicAck(envelope.getDeliveryTag, false)
//     }
//   }
//   channel.basicConsume(queue, false, consumer)
// }




def fromBytes(x: Array[Byte]) = new String(x, "UTF-8")

def toBytes(x: Long) = x.toString.getBytes("UTF-8")


def run (): Unit = {
  val subscriber = system.actorSelection("/user/akka-rabbitmq/subscriber1")

  val msgs = 0L to 30L
  msgs.foreach { x =>

    subscriber ! GetState
    println("request message:" + x + " ")
  }
}

