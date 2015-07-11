package jac3km4.wakfutcp

import java.net.InetSocketAddress
import java.nio.ByteBuffer

import akka.actor._
import akka.io.{IO, Tcp}
import akka.util.ByteString
import jac3km4.wakfutcp.WakfuTcpClient.{Inbound, Outbound}

object WakfuTcpClient {

  def props(handler: ActorRef, remote: InetSocketAddress) =
    Props(classOf[WakfuTcpClient], handler, remote)

  case class Inbound(data: ByteBuffer)

  case class Outbound(data: ByteBuffer)

}

class WakfuTcpClient(handler: ActorRef,
                     remote: InetSocketAddress)
  extends Actor with ActorLogging {

  import Tcp._
  import context._
  import jac3km4.wakfutcp.Util.Extensions._

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
        else
          handler ! Inbound(ByteBuffer.wrap(buf.getByteArray(length - 2)))
      } while (buf.limit > buf.position)
    case Outbound(data) =>
      connection ! Write(ByteString(data.array))
    case _: ConnectionClosed =>
      log.info("connection closed")
      connection ! Close
      stop(self)
    case _: Terminated =>
      log.info("terminating with listener")
      connection ! Close
      stop(self)
  }
}