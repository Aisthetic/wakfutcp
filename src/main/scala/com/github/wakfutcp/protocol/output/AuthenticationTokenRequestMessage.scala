package com.github.wakfutcp.protocol.output

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.{OutputMessage, OutputMessageWriter}

case class AuthenticationTokenRequestMessage
(
  serverId: Int,
  accountId: Long
  ) extends OutputMessage

object AuthenticationTokenRequestMessage
  extends OutputMessageWriter[AuthenticationTokenRequestMessage] {
  val id = 1211

  def write(msg: AuthenticationTokenRequestMessage) = pack(ByteBuffer
    .allocate(12)
    .putInt(msg.serverId)
    .putLong(msg.accountId)
    .array, 8)
}