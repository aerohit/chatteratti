package chatteratti.actors

import akka.actor.Actor
import akka.event.Logging
import akka.actor.Props

class UserActor extends Actor {
  val log = Logging(context.system, this)

  val console = context.actorOf(Props[ConsoleActor], "console")

  def receive = {
    case message: UserProtocol => message match {
      case Begin() =>
        log.debug("Enabling console")
        console ! EnableConsole()

      case MessageFromConsole(msgFromConsole) =>
        msgFromConsole match {
          case "done" =>
            log.debug("done received")
            context.parent ! KillChat

          case "stop" =>
            log.debug("stop received")
            context.parent ! StopChatting

          case "start" =>
            log.debug("start received")
            context.parent ! StartChatting

          case _ =>
            context.parent ! Speak(msgFromConsole)
            log.debug(s"Message from console received: $msgFromConsole")
        }

      case Speak(text) =>
        val formattedText = s"\n${sender().path.name}:\n$text"
        println(formattedText)
    }
  }
}
