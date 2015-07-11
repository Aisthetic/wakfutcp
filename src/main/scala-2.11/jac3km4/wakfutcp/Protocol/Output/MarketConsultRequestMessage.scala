package jac3km4.wakfutcp.Protocol.Output

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

case class MarketConsultRequestMessage
(
  itemRefs: Array[Int],
  itemType: Short,
  minPrice: Int,
  maxPrice: Int,
  sortType: Byte,
  firstIdx: Short,
  lowestMode: Boolean
  ) extends OutputMessage

object MarketConsultRequestMessage
  extends OutputMessageWriter[MarketConsultRequestMessage] {
  val id = 15263

  def write(msg: MarketConsultRequestMessage) = {
    val buf = ByteBuffer.allocate(14 + msg.itemRefs.length * 4)
    buf.putShort(msg.itemType)
    buf.putInt(msg.minPrice)
    buf.putInt(msg.maxPrice)
    buf.put(msg.sortType)
    buf.putShort(msg.firstIdx)
    buf.put((if (msg.lowestMode) 1 else 0).toByte)
    msg.itemRefs.foreach(buf.putInt)
    pack(buf.array, 3)
  }
}