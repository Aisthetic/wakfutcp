package com.github.wakfutcp.protocol.output

import com.github.wakfutcp.protocol.{OutputMessage, OutputMessageWriter}

case class ClientProxiesRequestMessage() extends OutputMessage

object ClientProxiesRequestMessage
  extends OutputMessageWriter[ClientProxiesRequestMessage] {
  val id = 1035

  def write(msg: ClientProxiesRequestMessage) =
    pack(Array.empty[Byte], 8)
}