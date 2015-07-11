package jac3km4.wakfutcp.Protocol.Output

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}


case class ClientAuthenticationTokenMessage
(
  token: String
  ) extends OutputMessage


object ClientAuthenticationTokenMessage
  extends OutputMessageWriter[ClientAuthenticationTokenMessage] {
  val id = 1213

  def write(msg: ClientAuthenticationTokenMessage) = {
    val buf = ByteBuffer.allocate(4 + msg.token.length)
    buf.putInt(msg.token.length)
    buf.put(msg.token.getBytes("UTF-8"))
    pack(buf.array, 1)
  }
}