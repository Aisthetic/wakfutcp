package com.github.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.Domain.Position
import com.github.wakfutcp.Protocol.{InputMessage, InputMessageReader}

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