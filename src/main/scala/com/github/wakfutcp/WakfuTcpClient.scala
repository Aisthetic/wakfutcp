package com.github.wakfutcp

import java.net.InetSocketAddress

import akka.actor._
import akka.io.{IO, Tcp}
import akka.util.ByteString
import com.github.wakfutcp.WakfuTcpClient.Disconnect
import com.github.wakfutcp.protocol.InputMessageReader
import com.github.wakfutcp.protocol.raw.input._

object WakfuTcpClient {
  def props(handler: ActorRef, remote: InetSocketAddress) =
    Props(classOf[WakfuTcpClient], handler, remote)

  case object Disconnect
}

class WakfuTcpClient(val handler: ActorRef,
                     remote: InetSocketAddress)
  extends Actor with ActorLogging {

  import Tcp._
  import context._

  IO(Tcp) ! Connect(remote)
  watch(handler)

  def receive = {
    case CommandFailed(_: Connect) ⇒
      log.error("connection failed")
      stop(self)
    case c@Connected(r, l) ⇒
      log.info("connection established with {}", r)
      handler ! c
      val connection = sender()
      connection ! Register(self)
      become(connected(connection))
  }

  def connected(connection: ActorRef): Receive = {
    case CommandFailed(w: Write) ⇒
      log.error("write failed")
    case Received(data) ⇒
      val received = data.asByteBuffer
      val length = received.getShort
      if (length <= received.limit && length > 2) {
        val id = received.getShort
        getReader(id) match {
          case Some(reader) ⇒
            val buf = received.slice()
            buf.limit(length - 4)
            handler ! reader.read(buf)
          case None ⇒
        }
      }
    case data: ByteString ⇒
      connection ! Write(data)
    case _: ConnectionClosed ⇒
      log.info("connection closed")
      stop(self)
    case Disconnect ⇒
      connection ! Close
  }

  private def getReader(id: Short): Option[InputMessageReader[_]] = id match {
    case 6 ⇒ Some(ForcedDisconnectionReasonMessage)
    case 8 ⇒ Some(ClientVersionResultMessage)
    case 110 ⇒ Some(ClientIpMessage)
    case 1025 ⇒ Some(ClientAuthenticationResultsMessage)
    case 1027 ⇒ Some(ClientDispatchAuthenticationResultMessage)
    case 1034 ⇒ Some(ClientPublicKeyMessage)
    case 1036 ⇒ Some(ClientProxiesResultMessage)
    case 1202 ⇒ Some(WorldSelectionResultMessage)
    case 1212 ⇒ Some(AuthenticationTokenResultMessage)
    case 2048 ⇒ Some(CharactersListMessage)
    case 2050 ⇒ Some(CharacterSelectionResultMessage)
    case 4102 ⇒ Some(ActorSpawnMessage)
    case 4127 ⇒ Some(ActorMoveToMessage)
    case 4524 ⇒ Some(FighterMoveMessage)
    case 7906 ⇒ Some(FightersPlacementPositionMessage)
    case 8100 ⇒ Some(TableTurnBeginMessage)
    case 8106 ⇒ Some(FighterTurnEndMessage)
    case 8300 ⇒ Some(EndFightMessage)
    case 20100 ⇒ Some(MarketConsultResultMessage)
    case _ ⇒ None
  }
}