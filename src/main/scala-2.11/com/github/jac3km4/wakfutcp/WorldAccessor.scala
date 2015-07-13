package com.github.jac3km4.wakfutcp

import akka.actor._
import akka.io.Tcp.Connected
import akka.util.ByteString
import com.github.jac3km4.wakfutcp.Protocol.Domain.Version
import com.github.jac3km4.wakfutcp.Protocol.Input._
import com.github.jac3km4.wakfutcp.Protocol.InputMessage
import com.github.jac3km4.wakfutcp.Protocol.Output._
import com.github.jac3km4.wakfutcp.WorldDispatcher.WorldAuthToken

import scala.concurrent.duration._

object WorldAccessor {
  def props(client: ActorRef, version: Version) =
    Props(classOf[WorldAccessor], client, version)
}

class WorldAccessor(val client: ActorRef, val version: Version)
  extends Actor with ActorLogging with Stash with Messenger {

  import context._

  val WorldHandleTimeout = 200.millis

  def receive = {
    case Connected(l, r) =>
      unstashAll()
      become(initializeConnection(sender()))
    case _ => stash()
  }

  def initializeConnection(connection: ActorRef): Receive = {
    case ClientIpMessage(_) =>
      connection ! wrap(ClientVersionMessage(version))

    case ClientVersionResultMessage(success, required) =>
      if (!success)
        throw ClientVersionException()
      unstashAll()
      become {
        case WorldAuthToken(tok) =>
          connection ! wrap(ClientAuthenticationTokenMessage(tok))
          unstashAll()
          become(enterWorld(connection))
        case _ => stash()
      }

    case _ => stash()
  }

  def enterWorld(connection: ActorRef): Receive = {
    case reason: ForcedDisconnectionReasonMessage =>
      throw ConnectionException(s"Force disconnected with $reason")

    case ClientAuthenticationResultsMessage.Success(_) =>
      log.info("game world authentication successful")

    case message: ClientAuthenticationResultsMessage =>
      throw AuthenticationException(s"Game world auth failed with $message")

    case WorldSelectionResultMessage.Success() =>
      log.info("game world selection successful")

    case WorldSelectionResultMessage.Failure() =>
      throw ConnectionException("Failed during world selection")

    case CharactersListMessage(characters) =>
      client ! CharacterList(characters)

    case CharacterSelectionResultMessage.Success() =>
      log.info("character selection successful, entering the game world in {}", WorldHandleTimeout)
      system.scheduler.scheduleOnce(WorldHandleTimeout, client, ConnectedToWorld())
      watch(client)
      become(connected(connection))

    case CharacterSelectionResultMessage.Failure() =>
      throw ConnectionException("Failed during character selection")

    // client
    case CharacterChoice(character) =>
      connection ! wrap(CharacterSelectionMessage(character.id, character.name))
  }

  def connected(connection: ActorRef): Receive = {
    case msg: InputMessage =>
      client ! msg
    case msg: ByteString =>
      connection forward msg
    case _: Terminated =>
      connection ! wrap(EndConnectionMessage())
      system.shutdown()
  }
}
