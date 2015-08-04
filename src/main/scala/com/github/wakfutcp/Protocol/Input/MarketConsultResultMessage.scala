package com.github.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.Domain.MarketEntry
import com.github.wakfutcp.Protocol.{InputMessage, InputMessageReader}
import com.github.wakfutcp.Util.Extensions
import sun.reflect.generics.reflectiveObjects.NotImplementedException

case class MarketConsultResultMessage
(
  sales: Array[MarketEntry],
  totalCount: Int
  ) extends InputMessage

object MarketConsultResultMessage
  extends InputMessageReader[MarketConsultResultMessage] {

  import Extensions._

  def read(buf: ByteBuffer): MarketConsultResultMessage = {
    buf.get match {
      case 0 =>
        val raw = ByteBuffer.wrap(buf.getByteArray_32)
        val count = buf.getInt
        MarketConsultResultMessage(
          Array.fill(raw.getInt)(MarketEntry.read(raw)),
          count
        )
      case 1 =>
        throw new NotImplementedException()
    }
  }
}