package com.github.wakfutcp.Protocol.Output

import com.github.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

case class ClientProxiesRequestMessage() extends OutputMessage

object ClientProxiesRequestMessage
  extends OutputMessageWriter[ClientProxiesRequestMessage] {
  val id = 1035

  def write(msg: ClientProxiesRequestMessage) =
    pack(Array.empty[Byte], 8)
}