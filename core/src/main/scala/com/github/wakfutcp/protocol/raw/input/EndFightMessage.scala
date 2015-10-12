package com.github.wakfutcp.protocol.raw.input

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.{InputMessage, InputMessageReader}

sealed trait EndFightMessage extends InputMessage

object EndFightMessage extends InputMessageReader[EndFightMessage] {
  def read(buf: ByteBuffer) = {
    val fightId = buf.getLong
    buf.getInt
    buf.getInt
    buf.get match {
      case 0 ⇒
        Finished(
          fightId,
          Array.fill(buf.get.toInt)(buf.getLong),
          Array.fill(buf.get.toInt)(buf.getLong),
          Array.fill(buf.get.toInt)(buf.getLong)
        )
      case 1 ⇒ Fleed(fightId)
    }
  }

  case class Finished
  (
    fightId: Long,
    winnerTeammates: Array[Long],
    looserTeammates: Array[Long],
    escapees: Array[Long]
    ) extends EndFightMessage

  case class Fleed
  (
    fightId: Long
    ) extends EndFightMessage

}