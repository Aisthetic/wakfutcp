package com.github.wakfutcp.protocol.raw.output

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.{OutputMessage, OutputMessageWriter}

case class FighterTurnEndAckMessage
(
  acknowledgedTurn: Int
  ) extends OutputMessage

object FighterTurnEndAckMessage extends OutputMessageWriter[FighterTurnEndAckMessage] {
  val id = 8112

  def write(msg: FighterTurnEndAckMessage) = pack(ByteBuffer
    .allocate(4)
    .putInt(msg.acknowledgedTurn)
    .array, 3)
}