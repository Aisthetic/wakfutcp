package jac3km4.wakfutcp

import akka.actor._
import akka.io.Tcp.Connected
import jac3km4.wakfutcp.Protocol.Domain.Version
import jac3km4.wakfutcp.Protocol.Input._
import jac3km4.wakfutcp.Protocol.Output._
import jac3km4.wakfutcp.WakfuTcpClient.{Inbound, Outbound}
import jac3km4.wakfutcp.WorldDispatcher.WorldAuthToken

import scala.concurrent.duration._

object WorldAccessor
  extends {
  def props(client: ActorRef, version: Version) =
    Props(classOf[WorldAccessor], client, version)
}

class WorldAccessor(client: ActorRef, version: Version)
  extends Actor with ActorLogging with Stash with Messenger {

  import context._

  def receive = {
    case Connected(l, r) =>
      unstashAll()
      become(initialize(sender()))
    case _ => stash()
  }

  def initialize(connection: ActorRef): Receive = {
    case Inbound(buf) =>
      val id = buf.getShort
      id match {
        case 110 =>
          connection ! wrap(ClientVersionMessage(version))
        case 8 =>
          val versionMessageResult = ClientVersionMessageResult.read(buf)
          unstashAll()
          become {
            case WorldAuthToken(tok) =>
              connection ! wrap(ClientAuthenticationTokenMessage(tok))
              unstashAll()
              become(enterWorld(connection))
            case _ => stash()
          }
        case _ =>
          buf.rewind
          stash()
      }
    case _ => stash()
  }

  def enterWorld(connection: ActorRef): Receive = {
    case Inbound(buf) =>
      val id = buf.getShort
      id match {
        case 6 =>
          val forcedDisconnectionMessage = ForcedDisconnectionReasonMessage.read(buf)
          println(forcedDisconnectionMessage)
        case 1025 =>
          val authenticationResultsMessage = ClientAuthenticationResultsMessage.read(buf)
        case 1202 =>
          val worldSelectionResultMessage = WorldSelectionResultMessage.read(buf)
        case 2048 =>
          val charactersListMessage = CharactersListMessage.read(buf)
          client ! CharacterList(charactersListMessage.characters)
        case 2050 =>
          val characterSelectionResultMessage = CharacterSelectionResultMessage.read(buf)
          // game server needs some time, otherwise doesn't answer
          system.scheduler.scheduleOnce(100.millis, client, ConnectedToWorld())
          become(connected(connection))
        case _ =>
      }
    case CharacterChoice(character) =>
      connection ! wrap(CharacterSelectionMessage(character.id, character.name))
  }

  def connected(connection: ActorRef): Receive = {
    case Inbound(buf) =>
      val id = buf.getShort
      id match {
        case 20100 =>
          client ! MarketConsultResultMessage.read(buf)
        case _ =>
      }
    case msg: Outbound =>
      connection ! msg
  }
}
