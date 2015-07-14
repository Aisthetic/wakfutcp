package com.github.wakfutcp

import java.net.InetSocketAddress
import java.nio.ByteBuffer

import akka.actor._
import akka.io.{IO, Tcp}
import akka.util.ByteString
import com.github.wakfutcp.Protocol.Input._
import com.github.wakfutcp.Protocol.InputMessageReader
import com.github.wakfutcp.Util.Extensions

object WakfuTcpClient {
  def props(handler: ActorRef, remote: InetSocketAddress) =
    Props(classOf[WakfuTcpClient], handler, remote)
}

class WakfuTcpClient(val handler: ActorRef,
                     remote: InetSocketAddress)
  extends Actor with ActorLogging {

  import Extensions._
  import Tcp._
  import context._

  IO(Tcp) ! Connect(remote)
  watch(handler)

  def receive = {
    case CommandFailed(_: Connect) =>
      log.error("connection failed")
      stop(self)
    case c@Connected(r, l) =>
      log.info("connection established with {}", r)
      handler ! c
      val connection = sender()
      connection ! Register(self)
      become(connected(connection))
  }

  /*
    messages are often in chunks,
    I process them one by one
  */
  def connected(connection: ActorRef): Receive = {
    case CommandFailed(w: Write) =>
      log.error("write failed")
    case Received(data) =>
      val buf = data.asByteBuffer
      do {
        val length = buf.getShort
        if (length < 5 || length > buf.limit - buf.position + 2)
          buf.position(buf.limit)
        else {
          val id = buf.getShort
          getReader(id) match {
            case Some(reader) =>
              val buffer = ByteBuffer.wrap(buf.getByteArray(length - 4))
              handler ! reader.read(buffer)
            case None =>
              buf.position(buf.position + length - 4)
          }
        }
      } while (buf.limit > buf.position)
    case data: ByteString =>
      connection ! Write(data)
    case _: ConnectionClosed =>
      log.info("connection closed")
      stop(self)
    case _: Terminated =>
      log.info("terminating with listener")
      connection ! Close
      stop(self)
  }

  private def getReader(id: Short): Option[InputMessageReader[_]] = id match {
    case 6 => Some(ForcedDisconnectionReasonMessage)
    case 8 => Some(ClientVersionResultMessage)
    case 110 => Some(ClientIpMessage)
    case 1025 => Some(ClientAuthenticationResultsMessage)
    case 1027 => Some(ClientDispatchAuthenticationResultMessage)
    case 1034 => Some(ClientPublicKeyMessage)
    case 1036 => Some(ClientProxiesResultMessage)
    case 1202 => Some(WorldSelectionResultMessage)
    case 1212 => Some(AuthenticationTokenResultMessage)
    case 2048 => Some(CharactersListMessage)
    case 2050 => Some(CharacterSelectionResultMessage)
    case 20100 => Some(MarketConsultResultMessage)
    case _ => None
  }
}