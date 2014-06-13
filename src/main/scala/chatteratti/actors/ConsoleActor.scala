package chatteratti.actors

import akka.actor.Actor
import akka.event.Logging
import scala.io.Source

class ConsoleActor extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case message: ConsoleProtocol => message match {
      case EnableConsole() =>
        log.debug("EnableConsole received")
        acceptUserInput()
    }
  }

  def acceptUserInput() = {
    println(
      """Please type something for your buddies and press enter!
Or, you can type:
stop, to disable chat
start, to restart it
or done, to exit this program!""")
    for (ln <- Source.stdin.getLines().takeWhile(!_.equals("done"))) {
      log.debug(s"Line = $ln")
      context.parent ! MessageFromConsole(ln)
    }
    context.parent ! MessageFromConsole("done")
  }
}