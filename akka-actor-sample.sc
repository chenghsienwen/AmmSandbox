import $ivy.`com.typesafe.akka::akka-http:10.1.1`
import $ivy.`com.typesafe.akka::akka-stream:2.5.12`
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

import sys.process._
import java.io.File
import ammonite.ops._, ImplicitWd._


class HelloActor extends Actor {
  def receive = {
    case "hello" => println("hello back at you")
    case _       => println("huh?")
  }
}
@main
def main() = {
  val system = ActorSystem("HelloSystem")
  // default Actor constructor
  val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")
  helloActor ! "hello"
  helloActor ! "buenos dias"
}

