package com.github.wakfutcp.Protocol.Output

import com.github.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

case class PartyInfoRequestMessage() extends OutputMessage

object PartyInfoRequestMessage
  extends OutputMessageWriter[PartyInfoRequestMessage] {
  val id = 525

  def write(msg: PartyInfoRequestMessage) =
    pack(Array.empty[Byte], 6)
}
