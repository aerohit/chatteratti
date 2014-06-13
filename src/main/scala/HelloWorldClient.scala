import io.backchat.hookup._
import java.net.URI
import akka.actor.ActorSystem
import scala.concurrent.duration._
import java.util.concurrent.atomic.AtomicInteger

object HelloWorldClient extends App {
  val uri = new URI("ws://localhost:8125")
  val system = ActorSystem("ChatClient")
  val messageCounter = new AtomicInteger(0)

  new DefaultHookupClient(HookupClientConfig(uri)) {

    def receive = {
      case Disconnected(_) ⇒
        println("The websocket to " + uri.toASCIIString + " disconnected.")
      case TextMessage(message) ⇒ {
        println("RECV: " + message)
        send("ECHO: " + message)
      }
    }

    connect() onSuccess {
      case Success ⇒
        println("The websocket is connected to:" + uri.toASCIIString + ".")
        system.scheduler.schedule(0 seconds, 1 second) {
          send("message " + messageCounter.incrementAndGet().toString)
        }
      case _ ⇒
    }
  }
}
