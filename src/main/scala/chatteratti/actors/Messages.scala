package chatteratti.actors

sealed trait ChatCoordinatorProtocol

case class NewChat() extends ChatCoordinatorProtocol

case class StartChatting() extends ChatCoordinatorProtocol

case class StopChatting() extends ChatCoordinatorProtocol

case class KillChat() extends ChatCoordinatorProtocol

sealed trait UserProtocol

sealed trait ChatParticipantsProtocol extends UserProtocol

case class Begin() extends ChatParticipantsProtocol

case class Speak(text: String) extends ChatParticipantsProtocol with ChatCoordinatorProtocol

case class MessageFromConsole(text: String) extends UserProtocol

sealed trait ConsoleProtocol

case class EnableConsole() extends ConsoleProtocol
