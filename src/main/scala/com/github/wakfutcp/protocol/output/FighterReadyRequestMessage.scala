package com.github.wakfutcp.protocol.output

import com.github.wakfutcp.protocol.{OutputMessage, OutputMessageWriter}

case class FighterReadyRequestMessage
(
  ready: Boolean
  ) extends OutputMessage

object FighterReadyRequestMessage extends OutputMessageWriter[FighterReadyRequestMessage] {
  val id = 8149

  def write(msg: FighterReadyRequestMessage) =
    pack(Array[Byte](if (msg.ready) 1 else 0), 3)
}