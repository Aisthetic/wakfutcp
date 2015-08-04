package com.github.wakfutcp.Protocol.Output

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

case class FighterEndTurnRequestMessage
(
  fighterId: Long,
  tableTurn: Short
  ) extends OutputMessage

object FighterEndTurnRequestMessage extends OutputMessageWriter[FighterEndTurnRequestMessage] {
  val id = 8105

  def write(msg: FighterEndTurnRequestMessage) = pack(ByteBuffer
    .allocate(10)
    .putLong(msg.fighterId)
    .putShort(msg.tableTurn)
    .array, 3)
}