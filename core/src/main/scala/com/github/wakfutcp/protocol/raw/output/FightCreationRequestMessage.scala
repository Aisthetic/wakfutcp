package com.github.wakfutcp.protocol.raw.output

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.domain.Position
import com.github.wakfutcp.protocol.{OutputMessage, OutputMessageWriter}

case class FightCreationRequestMessage
(
  targetId: Long,
  position: Position,
  lockInitially: Boolean
  ) extends OutputMessage

object FightCreationRequestMessage extends OutputMessageWriter[FightCreationRequestMessage] {
  val id = 8001

  def write(msg: FightCreationRequestMessage) = pack(ByteBuffer
    .allocate(19)
    .putLong(msg.targetId)
    .put(Position.write(msg.position).array)
    .put((if (msg.lockInitially) 1 else 0).toByte)
    .array, 3)
}