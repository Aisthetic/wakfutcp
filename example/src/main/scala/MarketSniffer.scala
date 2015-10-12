import akka.actor._
import com.github.wakfutcp.WakfuConnector.Credentials
import com.github.wakfutcp._
import com.github.wakfutcp.protocol.client.input._
import com.github.wakfutcp.protocol.client.output._
import com.github.wakfutcp.protocol.domain.MarketEntry
import com.github.wakfutcp.protocol.raw.input._
import com.github.wakfutcp.protocol.raw.output.Implicits._
import com.github.wakfutcp.protocol.raw.output._
import com.typesafe.config.ConfigFactory

object MarketSniffer {
  def props = Props[MarketSniffer]
}

class MarketSniffer
  extends Actor with Messenger with ActorLogging {

  import context._

  // instantiate the basic connection actor
  val connector = {
    val acc = ConfigFactory.load().getConfig("account")
    actorOf(
      Props(
        classOf[WakfuConnector],
        // reference to this actor
        self,
        // Credentials object with 2 fields - login and password
        Credentials(
          acc.getString("login"),
          acc.getString("password")
        ),
        /*
            passing None means that you want to use the default
            configuration from resources/application.conf
            in path "wakfutcp.default",
            you can also provide custom config object
        */
        None
      ))
  }

  def receive = {
    case ServerList(servers, _) ⇒
      sender() ! ServerChoice(servers.find(_.name == "Nox").get)
    case CharacterList(characters) ⇒
      sender() ! CharacterChoice(characters.find(_.name == "Jekki Chan").get)
    case ConnectedToWorld() ⇒
      /*
          the wrap function allows you to wrap
          a raw wakfu message, so you can send any
          kind of message that can be translated into bytes
      */
      sender() ! wrap(InteractiveElementActionMessage(12224, 12))
    case MarketConsultResultMessage(sales, count) ⇒
      sender() ! wrap(MarketConsultRequestMessage(Array.empty, -1, -1, -1, -1, 10.toShort, lowestMode = false))
      become(receiveAndAsk(sales.toList, 10))
  }

  /*
      market entries come by 10,
      here I gather them recursively
   */
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
    // do stuff with data and quit

    sender() ! LogOut
  }
}