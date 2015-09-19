package com.github.wakfutcp.protocol.output

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.domain.Position
import com.github.wakfutcp.protocol.{OutputMessage, OutputMessageWriter}

case class SpellLevelCastRequestMessage
(
  fighterId: Long,
  spellId: Long,
  castAt: Position
  ) extends OutputMessage

object SpellLevelCastRequestMessage extends OutputMessageWriter[SpellLevelCastRequestMessage] {
  val id = 8109

  def write(msg: SpellLevelCastRequestMessage) = pack(ByteBuffer
    .allocate(26)
    .putLong(msg.fighterId)
    .putLong(msg.spellId)
    .put(Position.write(msg.castAt).array)
    .array, 3)
}