package chatteratti

import akka.actor.ActorSystem
import akka.event.Logging
import akka.actor.Props
import chatteratti.actors.{NewChat, ChatCoordinator}

object Chatteratti extends App {

  val system = ActorSystem()

  val log = Logging(system, Chatteratti.getClass.getName)

  log.debug("Initializing chat system.")
  val manager = system.actorOf(Props[ChatCoordinator], "manager")
  manager ! NewChat
}