package com.github.wakfutcp.Protocol.Output

import com.github.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

case class ClientPublicKeyRequestMessage
(
  serverId: Byte
  ) extends OutputMessage

object ClientPublicKeyRequestMessage
  extends OutputMessageWriter[ClientPublicKeyRequestMessage] {
  val id = 1033

  def write(msg: ClientPublicKeyRequestMessage) =
    pack(Array.empty[Byte], msg.serverId)
}