package chatteratti

import akka.actor.ActorSystem
import akka.event.Logging
import akka.actor.Props
import chatteratti.actors.{NewChat, ChatCoordinator}

object Chatteratti {

  val system = ActorSystem()

  val log = Logging(system, Chatteratti.getClass.getName)

  def main(args: Array[String]): Unit = run()

  def run() = {
    log.debug("Initializing chat system.")
    val manager = system.actorOf(Props[ChatCoordinator], "manager")
    manager ! NewChat
  }
}