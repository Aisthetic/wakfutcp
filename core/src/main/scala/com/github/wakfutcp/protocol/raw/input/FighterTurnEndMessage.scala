package com.github.wakfutcp.protocol.raw.input

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.{InputMessage, InputMessageReader}

case class FighterTurnEndMessage
(
  fightId: Long,
  fighterId: Long,
  timeScoreGain: Int,
  addedRemainingSeconds: Int
  ) extends InputMessage

object FighterTurnEndMessage extends InputMessageReader[FighterTurnEndMessage] {
  def read(buf: ByteBuffer) = {
    val fightId = buf.getLong
    buf.getInt
    buf.getInt
    FighterTurnEndMessage(
      fightId,
      buf.getLong,
      buf.getInt,
      buf.getInt
    )
  }
}