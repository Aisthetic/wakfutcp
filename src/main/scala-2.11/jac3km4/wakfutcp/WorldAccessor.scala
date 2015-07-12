package jac3km4.wakfutcp

import akka.actor._
import akka.io.Tcp.Connected
import jac3km4.wakfutcp.Protocol.Domain.Version
import jac3km4.wakfutcp.Protocol.Input._
import jac3km4.wakfutcp.Protocol.Output._
import jac3km4.wakfutcp.WakfuTcpClient.{Inbound, Outbound}
import jac3km4.wakfutcp.WorldDispatcher.WorldAuthToken

import scala.concurrent.duration._

object WorldAccessor {
  def props(client: ActorRef, version: Version) =
    Props(classOf[WorldAccessor], client, version)
}

class WorldAccessor(client: ActorRef, version: Version)
  extends Actor with ActorLogging with Stash with Messenger {

  import context._

  val WorldHandleTimeout = 100.millis

  /*
    mesages incoming during the server session
    should be processed here
   */
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
          val versionResult = ClientVersionResultMessage.read(buf)
          if (!versionResult.success) {
            log.error("invalid client version, required is {}", versionResult.required)
            stop(self)
          }
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
          val message = ForcedDisconnectionReasonMessage.read(buf)
          log.error("iorce disconnected with {}", message)
          stop(self)
        case 1025 =>
          import ClientAuthenticationResultsMessage._
          ClientAuthenticationResultsMessage.read(buf) match {
            case Success(_) =>
              log.info("game world authentication successful")
            case message =>
              log.error("authentication failed with {}", message)
              stop(self)
          }
        case 1202 =>
          import WorldSelectionResultMessage._
          WorldSelectionResultMessage.read(buf) match {
            case Success() =>
              log.info("game world selection successful")
            case Failure() =>
              log.error("failed during world selection")
              stop(self)
          }
        case 2048 =>
          val charactersList = CharactersListMessage.read(buf)
          client ! CharacterList(charactersList.characters)
        case 2050 =>
          import jac3km4.wakfutcp.Protocol.Input.CharacterSelectionResultMessage._
          CharacterSelectionResultMessage.read(buf) match {
            case Success() =>
              // game server needs some time, otherwise doesn't answer
              log.info("character selection successful, entering the game world in {}", WorldHandleTimeout)
              system.scheduler.scheduleOnce(WorldHandleTimeout, client, ConnectedToWorld())
              become(connected(connection))
            case Failure() =>
              log.error("failed during character selection")
              stop(self)
          }
        case _ =>
      }
    case CharacterChoice(character) =>
      connection ! wrap(CharacterSelectionMessage(character.id, character.name))
  }
}
