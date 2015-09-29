import java.net.InetSocketAddress

import akka.actor._
import com.github.wakfutcp.WakfuConnector.Credentials
import com.github.wakfutcp._
import com.github.wakfutcp.protocol.client.input._
import com.github.wakfutcp.protocol.client.output._
import com.github.wakfutcp.protocol.domain.MarketEntry
import com.github.wakfutcp.protocol.raw.input._
import com.github.wakfutcp.protocol.raw.output._
import com.typesafe.config.ConfigFactory

object MarketSniffer {
  def props = Props[MarketSniffer]
}

class MarketSniffer
  extends Actor with Messenger with ActorLogging {

  import context._

  val connector = {
    val factory = ConfigFactory.load()
    val account = factory.getConfig("account")
    val gate = factory.getConfig("gate")
    actorOf(Props(
      classOf[WakfuConnector],
      self,
      new InetSocketAddress(gate.getString("address"), gate.getInt("port")),
      Credentials(account.getString("login"), account.getString("password")))
    )
  }

  def receive = {
    case ServerList(servers, _) ⇒
      sender() ! ServerChoice(servers.find(_.name == "Nox").get)
    case CharacterList(characters) ⇒
      sender() ! CharacterChoice(characters.find(_.name == "Jekki Chan").get)
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
    // do stuff with data

    sender() ! LogOut
  }
}