package com.github.jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.jac3km4.wakfutcp.Protocol.Domain.MarketEntry
import com.github.jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}
import sun.reflect.generics.reflectiveObjects.NotImplementedException

case class MarketConsultResultMessage
(
  sales: Array[MarketEntry],
  totalCount: Int
  ) extends InputMessage

object MarketConsultResultMessage
  extends InputMessageReader[MarketConsultResultMessage] {

  import com.github.jac3km4.wakfutcp.Util.Extensions._

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