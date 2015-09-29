package com.github.wakfutcp.protocol.raw.input

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.domain.Position
import com.github.wakfutcp.protocol.{InputMessage, InputMessageReader}

case class ActorMoveToMessage
(
  actorId: Long,
  position: Position,
  direction: Byte
  ) extends InputMessage

object ActorMoveToMessage extends InputMessageReader[ActorMoveToMessage] {
  def read(buf: ByteBuffer) = ActorMoveToMessage(
    buf.getLong,
    Position.read(buf),
    buf.get
  )
}