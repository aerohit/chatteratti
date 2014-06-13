package chatteratti.actors

import akka.actor.Actor
import akka.actor.FSM
import akka.actor.Props
import akka.actor.ActorRef

class ChatCoordinator extends Actor with FSM[ChatCoordinatorState, ChatCoordinatorData] {
  startWith(ChatOfflineState, UninitializedData)

  when(ChatOfflineState) {
    case Event(NewChat, UninitializedData) =>
      log.debug("NewChat received while in ChatOffline state.")
      val user = context.actorOf(Props[UserActor], "user")
      val chatBot = context.actorOf(Props[FortuneActor], "fortuneteller")
      goto(ChatOnlineState) using ChatData(List(user, chatBot), List[String]())

    case Event(StartChatting, ChatData(chatters, _)) =>
      log.debug("StartChatting received while in ChatOffline state.")
      goto(ChatOnlineState) using ChatData(chatters, List[String]())
  }

  when(ChatOnlineState) {
    case Event(Speak(text), chatData@ChatData(chatters, msgsSoFar)) =>
      log.debug(s"Message($text) event received while in ChatOnline state. chatData=$chatData")
      (chatters diff List(sender())).foreach(_ forward Speak(text))
      val labeledText = sender().path.name + ": " + text
      stay using ChatData(chatters, msgsSoFar :+ labeledText)

    case Event(StopChatting, ChatData(chatters, _)) =>
      log.debug("StopChat event received while in ChatOnline state.")
      goto(ChatOfflineState) using ChatData(chatters, Nil)
  }

  whenUnhandled {
    case Event(KillChat, ChatData(_, msgsIfAny)) =>
      log.info("Chat shutting down")
      println("Shutting down...\n\n" +
        "-- Begin server chat log --")
      msgsIfAny.foreach(println(_))
      println("-- End server chat log --")
      context.system.shutdown()
      stay()

    case Event(e, s) =>
      log.warning(s"received unhandled request $e in state $stateName/$s")
      stay()
  }

  onTransition {
    case ChatOfflineState -> ChatOnlineState =>
      for ((UninitializedData, ChatData(chatters, _)) <- Some(stateData, nextStateData)) {
        chatters.foreach(_ ! Begin())
      }
  }
}

sealed trait ChatCoordinatorState

case object ChatOfflineState extends ChatCoordinatorState

case object ChatOnlineState extends ChatCoordinatorState

sealed trait ChatCoordinatorData

case object UninitializedData extends ChatCoordinatorData

case class ChatData(chatters: List[ActorRef], messages: List[String]) extends ChatCoordinatorData