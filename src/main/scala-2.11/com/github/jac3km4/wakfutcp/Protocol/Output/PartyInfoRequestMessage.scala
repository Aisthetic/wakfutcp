package com.github.jac3km4.wakfutcp.Protocol.Output

import com.github.jac3km4.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

case class PartyInfoRequestMessage() extends OutputMessage

object PartyInfoRequestMessage
  extends OutputMessageWriter[PartyInfoRequestMessage] {
  val id = 525

  def write(msg: PartyInfoRequestMessage) =
    pack(Array.empty[Byte], 6)
}
