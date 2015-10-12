package com.github.wakfutcp.protocol.raw.output

import com.github.wakfutcp.protocol.{OutputMessage, OutputMessageWriter}

case class PartyInfoRequestMessage() extends OutputMessage

object PartyInfoRequestMessage
  extends OutputMessageWriter[PartyInfoRequestMessage] {
  val id = 525

  def write(msg: PartyInfoRequestMessage) =
    pack(Array.empty[Byte], 6)
}
