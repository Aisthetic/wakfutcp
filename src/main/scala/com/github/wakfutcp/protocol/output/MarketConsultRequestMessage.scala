package com.github.wakfutcp.protocol.output

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.{OutputMessage, OutputMessageWriter}

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
    val buf = ByteBuffer
      .allocate(14 + msg.itemRefs.length * 4)
      .putShort(msg.itemType)
      .putInt(msg.minPrice)
      .putInt(msg.maxPrice)
      .put(msg.sortType)
      .putShort(msg.firstIdx)
      .put((if (msg.lowestMode) 1 else 0).toByte)
    msg.itemRefs.foreach(buf.putInt)
    pack(buf.array, 3)
  }
}