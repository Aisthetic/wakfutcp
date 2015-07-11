package jac3km4.wakfutcp

import akka.actor.{Actor, ActorRef, Props, Stash}
import jac3km4.wakfutcp.Protocol.Input._
import jac3km4.wakfutcp.Protocol.Output._

/*
  example front-end client
  browsing the market
 */

object Mallrat {
  def props = Props[Mallrat]
}

class Mallrat
  extends Actor with Messenger with Stash {

  import context._

  def receive = {
    case ServerList(servers, _) =>
      sender() ! ServerChoice(servers.find(_.name == "Nox").get)
    case CharacterList(characters) =>
      sender() ! CharacterChoice(characters.find(_.name == "Derp").get)
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
      become(receiveSales(sender()))
    case _ => stash()
  }

  def receiveSales(connection: ActorRef): Receive = {
    case MarketConsultResultMessage(sales, count) =>
    // do something with them...
  }
}