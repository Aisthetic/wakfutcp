package com.github.wakfutcp

import java.net.InetSocketAddress

import akka.actor._
import akka.io.Tcp.Connected
import akka.util.ByteString
import com.github.wakfutcp.Exceptions._
import com.github.wakfutcp.WakfuConnector.WorldAuthToken
import com.github.wakfutcp.WakfuTcpClient.Disconnect
import com.github.wakfutcp.WakfuWorldAccessor._
import com.github.wakfutcp.protocol.InputMessage
import com.github.wakfutcp.protocol.client.input._
import com.github.wakfutcp.protocol.client.output._
import com.github.wakfutcp.protocol.domain.Version
import com.github.wakfutcp.protocol.raw.input._
import com.github.wakfutcp.protocol.raw.output.Implicits._
import com.github.wakfutcp.protocol.raw.output._

import scala.concurrent.duration._

object WakfuWorldAccessor {
  def props(client: ActorRef, address: InetSocketAddress, version: Version) =
    Props(classOf[WakfuWorldAccessor], client, address, version)

  sealed trait State

  case object Await extends State

  case object EnterWorld extends State

  case object MaintainConnection extends State

}

class WakfuWorldAccessor(val client: ActorRef,
                         address: InetSocketAddress,
                         version: Version)
  extends FSM[State, Unit] with Stash with ActorLogging with Messenger {

  import context._

  val connection = actorOf(Props(classOf[WakfuTcpClient], self, address))

  val WorldHandleTimeout = 500.millis

  startWith(Await, ())

  when(Await) {
    case Event(_: Connected, _) ⇒
      stay()

    case Event(_: ClientIpMessage, _) ⇒
      connection ! wrap(ClientVersionMessage(version))
      stay()

    case Event(ClientVersionResultMessage(success, required), _) ⇒
      if (!success)
        throw ClientVersionException()
      unstashAll()
      goto(EnterWorld)

    case Event(_: WorldAuthToken, _) ⇒
      stash()
      stay()
  }

  when(EnterWorld) {
    case Event(WorldAuthToken(tok), _) ⇒
      connection ! wrap(ClientAuthenticationTokenMessage(tok))
      stay()

    case Event(reason: ForcedDisconnectionReasonMessage, _) ⇒
      throw ConnectionException(s"Force disconnected with $reason")

    case Event(ClientAuthenticationResultsMessage.Success(_), _) ⇒
      log.info("game world authentication successful")
      stay()

    case Event(message: ClientAuthenticationResultsMessage, _) ⇒
      throw AuthenticationException(s"Game world auth failed with $message")

    case Event(WorldSelectionResultMessage.Success, _) ⇒
      log.info("game world selection successful")
      stay()

    case Event(WorldSelectionResultMessage.Failure, _) ⇒
      throw ConnectionException("Failed during world selection")

    case Event(CharactersListMessage(characters), _) ⇒
      client ! CharacterList(characters)
      stay()

    case Event(CharacterSelectionResultMessage.Success, _) ⇒
      log.info("character selection successful, entering the game world in {}", WorldHandleTimeout)
      system.scheduler.scheduleOnce(WorldHandleTimeout, client, ConnectedToWorld())
      goto(MaintainConnection)

    case Event(CharacterSelectionResultMessage.Failure, _) ⇒
      throw ConnectionException("Failed during character selection")

    // client
    case Event(CharacterChoice(character), _) ⇒
      connection ! wrap(CharacterSelectionMessage(character.id, character.name))
      stay()

    case Event(LogOut, _) ⇒
      log.info("logging out")
      connection ! wrap(EndConnectionMessage())
      connection ! Disconnect
      parent ! LogOut
      stop()
  }

  when(MaintainConnection) {
    case Event(msg: InputMessage, _) ⇒
      client ! msg
      stay()

    case Event(msg: ByteString, _) ⇒
      connection forward msg
      stay()

    case Event(LogOut, _) ⇒
      log.info("logging out")
      connection ! wrap(EndConnectionMessage())
      connection ! Disconnect
      parent ! LogOut
      stop()
  }
}
