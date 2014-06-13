import io.backchat.hookup._

object HelloWorldServer extends App {

  val server = HookupServer(8125) {
    new HookupServerClient {
      def receive = {
        case TextMessage(text) =>
          println(text)
          send(text)
      }
    }
  }

  server.start
}
