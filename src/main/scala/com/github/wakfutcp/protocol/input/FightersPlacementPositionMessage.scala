package com.github.wakfutcp.protocol.input

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.domain.Position
import com.github.wakfutcp.protocol.{InputMessage, InputMessageReader}

case class FightersPlacementPositionMessage
(
  fightId: Int,
  fighters: Array[FighterPosition],
  shouldTeleport: Boolean
  ) extends InputMessage

case class FighterPosition(id: Long, position: Position)

object FightersPlacementPositionMessage extends InputMessageReader[FightersPlacementPositionMessage] {
  def read(buf: ByteBuffer) = FightersPlacementPositionMessage(
    buf.getInt,
    Array.fill(buf.getShort) {
      FighterPosition(buf.getLong, Position.read(buf))
    },
    buf.get == 1
  )
}