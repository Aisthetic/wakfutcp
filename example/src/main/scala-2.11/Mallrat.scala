import akka.actor.{Actor, ActorRef, Props, Stash}
import com.github.jac3km4.wakfutcp.Protocol.Input._
import com.github.jac3km4.wakfutcp.Protocol.Output._
import com.github.jac3km4.wakfutcp._

object Mallrat {
  def props = Props[Mallrat]
}

class Mallrat extends Actor with Stash with Messenger {
  def receive = {
    case ServerList(servers, _) =>
      sender() ! ServerChoice(servers.find(_.name == "Nox").get)
    case CharacterList(characters) =>
      sender() ! CharacterChoice(characters.find(_.name == "Jekki Chan").get)
    case ConnectedToWorld() =>
      /*
        command used to check the market
        on the astrub board
       */
      sender() ! wrap(InteractiveElementActionMessage(18744, 12))
    case MarketConsultResultMessage(sales, count) =>
      /*
        market items come by 10
       */
      for (i <- 10 until count by 10)
        sender() ! wrap(MarketConsultRequestMessage(Array.empty, -1, -1, -1, -1, i.toShort, lowestMode = true))
      unstashAll()
      context.become(receiveSales(sender()))
    case _ => stash()
  }

  def receiveSales(connection: ActorRef): Receive = {
    case MarketConsultResultMessage(sales, count) =>
      println(sales.foreach(println))
    // do something with them...
  }
}
