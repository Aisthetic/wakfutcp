package com.github.wakfutcp

import akka.actor._
import akka.io.Tcp.Connected
import akka.util.ByteString
import com.github.wakfutcp.Protocol.Domain.Version
import com.github.wakfutcp.Protocol.Input._
import com.github.wakfutcp.Protocol.InputMessage
import com.github.wakfutcp.Protocol.Output._
import com.github.wakfutcp.WorldAccessor._
import com.github.wakfutcp.WorldDispatcher.WorldAuthToken

import scala.concurrent.duration._

object WorldAccessor {
  def props(client: ActorRef, v: Version) =
    Props(classOf[WorldAccessor], client, v)

  // states
  case object Uninitialized extends Data

  case class ConnectionData(connection: ActorRef) extends Data

  case object Authenticate extends State

  case object EnterWorld extends State

  case object MaintainConnection extends State

}

class WorldAccessor(val client: ActorRef,
                    val version: Version)
  extends FSM[State, Data] with Stash with ActorLogging with Messenger {

  import context._

  val WorldHandleTimeout = 500.millis

  startWith(Authenticate, Uninitialized)

  when(Authenticate) {
    case Event(_: Connected, _) =>
      stay using ConnectionData(sender())

    case Event(ClientIpMessage(_), ConnectionData(con)) =>
      con ! wrap(ClientVersionMessage(version))
      stay()

    case Event(ClientVersionResultMessage(success, required), _) =>
      if (!success)
        throw ClientVersionException()
      unstashAll()
      stay()

    case Event(_: WorldAuthToken, Uninitialized) =>
      stash()
      stay()

    case Event(WorldAuthToken(tok), ConnectionData(con)) =>
      con ! wrap(ClientAuthenticationTokenMessage(tok))
      goto(EnterWorld)
  }

  when(EnterWorld) {
    case Event(reason: ForcedDisconnectionReasonMessage, _) =>
      throw ConnectionException(s"Force disconnected with $reason")

    case Event(ClientAuthenticationResultsMessage.Success(_), _) =>
      log.info("game world authentication successful")
      stay()

    case Event(message: ClientAuthenticationResultsMessage, _) =>
      throw AuthenticationException(s"Game world auth failed with $message")

    case Event(WorldSelectionResultMessage.Success, _) =>
      log.info("game world selection successful")
      stay()

    case Event(WorldSelectionResultMessage.Failure, _) =>
      throw ConnectionException("Failed during world selection")

    case Event(CharactersListMessage(characters), _) =>
      client ! CharacterList(characters)
      stay()

    case Event(CharacterSelectionResultMessage.Success, _) =>
      log.info("character selection successful, entering the game world in {}", WorldHandleTimeout)
      system.scheduler.scheduleOnce(WorldHandleTimeout, client, ConnectedToWorld())
      watch(client)
      goto(MaintainConnection)

    case Event(CharacterSelectionResultMessage.Failure, _) =>
      throw ConnectionException("Failed during character selection")

    // client
    case Event(CharacterChoice(character), ConnectionData(con)) =>
      con ! wrap(CharacterSelectionMessage(character.id, character.name))
      stay()
  }

  when(MaintainConnection) {
    case Event(msg: InputMessage, _) =>
      client ! msg
      stay()

    case Event(msg: ByteString, ConnectionData(con)) =>
      con forward msg
      stay()

    case Event(_: Terminated, ConnectionData(con)) =>
      con ! wrap(EndConnectionMessage())
      stop()
  }
}
