package com.github.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.{InputMessage, InputMessageReader}

case class TableTurnBeginMessage
(
  numTurns: Short,
  timeline: Array[Byte]
  ) extends InputMessage

object TableTurnBeginMessage extends InputMessageReader[TableTurnBeginMessage] {

  import com.github.wakfutcp.Util.Extensions._

  def read(buf: ByteBuffer) = {
    buf.getInt
    buf.getInt
    buf.getInt
    TableTurnBeginMessage(
      buf.getShort,
      if (buf.remaining() > 0)
        buf.getByteArray_16
      else Array.empty
    )
  }
}