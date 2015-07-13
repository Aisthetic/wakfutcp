package com.github.jac3km4.wakfutcp.Protocol.Output

import java.nio.ByteBuffer

import com.github.jac3km4.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

case class AuthenticationTokenRequestMessage
(
  serverId: Int,
  accountId: Long
  ) extends OutputMessage

object AuthenticationTokenRequestMessage
  extends OutputMessageWriter[AuthenticationTokenRequestMessage] {
  val id = 1211

  def write(msg: AuthenticationTokenRequestMessage) = {
    val buf = ByteBuffer.allocate(12)
    buf.putInt(msg.serverId)
    buf.putLong(msg.accountId)
    pack(buf.array, 8)
  }
}