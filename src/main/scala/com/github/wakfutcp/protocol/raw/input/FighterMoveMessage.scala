package com.github.wakfutcp.protocol.raw.input

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.domain.Position
import com.github.wakfutcp.protocol.{InputMessage, InputMessageReader}

case class FighterMoveMessage
(
  fightId: Long,
  fighterId: Long,
  direction: Byte,
  position: Position,
  movementType: Byte
  ) extends InputMessage

object FighterMoveMessage extends InputMessageReader[FighterMoveMessage] {
  def read(buf: ByteBuffer) = {
    val fightId = buf.getLong
    buf.getInt
    buf.getInt
    val fighterId = buf.getLong
    val direction = buf.get
    val movementType = buf.get
    val skip = buf.limit - 22
    buf.position(buf.position + skip - 10)
    FighterMoveMessage(
      fightId,
      fighterId,
      direction,
      Position.read(buf),
      movementType
    )
  }
}