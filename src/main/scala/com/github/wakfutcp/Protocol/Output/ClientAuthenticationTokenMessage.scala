package com.github.wakfutcp.Protocol.Output

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

case class ClientAuthenticationTokenMessage
(
  token: String
  ) extends OutputMessage


object ClientAuthenticationTokenMessage
  extends OutputMessageWriter[ClientAuthenticationTokenMessage] {
  val id = 1213

  def write(msg: ClientAuthenticationTokenMessage) = pack(ByteBuffer
    .allocate(4 + msg.token.length)
    .putInt(msg.token.length)
    .put(msg.token.getBytes("UTF-8"))
    .array, 1)
}