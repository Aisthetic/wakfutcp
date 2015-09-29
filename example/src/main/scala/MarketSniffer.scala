import akka.actor._
import com.github.wakfutcp._
import com.github.wakfutcp.protocol.client.input._
import com.github.wakfutcp.protocol.client.output._
import com.github.wakfutcp.protocol.domain._
import com.github.wakfutcp.protocol.raw.input._
import com.github.wakfutcp.protocol.raw.output._

object MarketSniffer {
  def props = Props[MarketSniffer]
}

class MarketSniffer extends Actor with Messenger with ActorLogging {

  import context._

  def receive = {
    case ServerList(servers, _) ⇒
      sender() ! ServerChoice(servers.find(_.name == "Nox").get)
    case CharacterList(characters) ⇒
      sender() ! CharacterChoice(characters.find(_.name == "Derp").get)
    case ConnectedToWorld() ⇒
      sender() ! wrap(InteractiveElementActionMessage(12224, 12))
    case MarketConsultResultMessage(sales, count) ⇒
      sender() ! wrap(MarketConsultRequestMessage(Array.empty, -1, -1, -1, -1, 10.toShort, lowestMode = false))
      become(receiveAndAsk(sales.toList, 10))
  }

  def receiveAndAsk(entries: List[MarketEntry], start: Int): Receive = {
    case MarketConsultResultMessage(sales, count) ⇒
      val next = start + 10
      if (next >= count)
        handleData(entries ::: sales.toList)
      else {
        sender() ! wrap(MarketConsultRequestMessage(Array.empty, -1, -1, -1, -1, next.toShort, lowestMode = false))
        become(receiveAndAsk(entries ::: sales.toList, next))
      }
  }

  def handleData(data: List[MarketEntry]) = {
    // stuff
    stop(self)
  }
}