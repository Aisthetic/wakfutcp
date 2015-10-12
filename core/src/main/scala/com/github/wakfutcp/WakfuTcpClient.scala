package com.github.wakfutcp

import java.net.InetSocketAddress
import java.nio.ByteBuffer

import akka.actor._
import akka.io.{IO, Tcp}
import akka.util.ByteString
import com.github.wakfutcp.WakfuTcpClient.Disconnect
import com.github.wakfutcp.protocol.raw.input.Implicits._
import com.github.wakfutcp.protocol.raw.input._
import com.github.wakfutcp.protocol.{InputMessage, InputMessageReader}

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
      val bb = data.asByteBuffer
      val length = bb.getShort
      if (length <= bb.limit && length > 2) {
        val msgId = bb.getShort
        val msgBb = bb.slice()
        msgBb.limit(length - 4)
        msgId match {
          case 6 ⇒ forward[ForcedDisconnectionReasonMessage](msgBb)
          case 8 ⇒ forward[ClientVersionResultMessage](msgBb)
          case 110 ⇒ forward[ClientIpMessage](msgBb)
          case 1025 ⇒ forward[ClientAuthenticationResultsMessage](msgBb)
          case 1027 ⇒ forward[ClientDispatchAuthenticationResultMessage](msgBb)
          case 1034 ⇒ forward[ClientPublicKeyMessage](msgBb)
          case 1036 ⇒ forward[ClientProxiesResultMessage](msgBb)
          case 1202 ⇒ forward[WorldSelectionResultMessage](msgBb)
          case 1212 ⇒ forward[AuthenticationTokenResultMessage](msgBb)
          case 2048 ⇒ forward[CharactersListMessage](msgBb)
          case 2050 ⇒ forward[CharacterSelectionResultMessage](msgBb)
          case 4102 ⇒ forward[ActorSpawnMessage](msgBb)
          case 4127 ⇒ forward[ActorMoveToMessage](msgBb)
          case 4524 ⇒ forward[FighterMoveMessage](msgBb)
          case 7906 ⇒ forward[FightersPlacementPositionMessage](msgBb)
          case 8100 ⇒ forward[TableTurnBeginMessage](msgBb)
          case 8106 ⇒ forward[FighterTurnEndMessage](msgBb)
          case 8300 ⇒ forward[EndFightMessage](msgBb)
          case 20100 ⇒ forward[MarketConsultResultMessage](msgBb)
          case id ⇒
            log.info(s"unknown message kind: $id")
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

  def forward[T <: InputMessage : InputMessageReader](bb: ByteBuffer): Unit =
    handler ! implicitly[InputMessageReader[T]].read(bb)
}