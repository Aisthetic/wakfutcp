package com.github.wakfutcp.Protocol.Output

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.Domain.Position
import com.github.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

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