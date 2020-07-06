import $ivy.`com.typesafe.akka::akka-http:10.1.1`
import $ivy.`com.typesafe.akka::akka-stream:2.5.12`
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.PoisonPill
import sys.process._
import java.io.File
import ammonite.ops._, ImplicitWd._

import scala.util.Random
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

case object Produce

class Parent extends Actor {
  override def receive: Receive = {
    case Produce =>
      val child = context.system.actorOf(Props[Child])
      child ! Produce
  }
}

class Child extends Actor {
  val data = new Array[Byte](2000000)

  override def receive: Receive = {
    case Produce =>
      Random.nextBytes(data)
      context.system.scheduler.scheduleOnce(1 second, self, PoisonPill)
  }
}

@main
def main() = {
  val actorSystem = ActorSystem()
  val actor = actorSystem.actorOf(Props[Parent])
 actorSystem.scheduler.schedule(2 seconds, 500 millis, actor, Produce)
  Console.println("Press enter")
  Console.readLine() //I need a few seconds to attach VisualVM to the process ;)

  
}