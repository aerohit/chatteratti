package chatteratti.actors

import akka.actor.Actor
import akka.event.Logging
import scala.util.Random

class OpinionBot extends Actor {
  val log = Logging(context.system, this)

  val rand = new Random(System.currentTimeMillis() + self.path.hashCode)

  def receive = {
    case message: ChatParticipantsProtocol => message match {
      case Speak(msg) =>
        val senderName: String = sender().path.name
        if (senderName.equals("user")) {
          log.debug(s"${this.getClass.getSimpleName} ${self.path.name} received $msg")
          context.parent ! Speak(FortuneTeller.fortune())
        } else {
          log.debug(s"${this.getClass.getSimpleName} ignored speaking of $msg  from  $senderName.")
        }
      case Begin() =>
        log.debug(s"${this.getClass.getSimpleName} ignored begin instruction.")
    }
  }
}

object FortuneTeller {

  import sys.process._

  def fortune(): String = "fortune".!!
}