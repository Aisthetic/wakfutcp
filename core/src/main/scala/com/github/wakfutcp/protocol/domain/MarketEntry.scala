package com.github.wakfutcp.protocol.domain

import java.nio.ByteBuffer

import com.github.nscala_time.time.Imports._

case class MarketEntry
(
  id: Long,
  sellerId: Long,
  sellerName: String,
  packTypeId: Byte,
  packNumber: Short,
  packPrice: Int,
  durationId: Byte,
  releaseDate: DateTime,
  rawItem: RawInventoryItem
  ) extends DataObject

object MarketEntry extends DataObjectReader[MarketEntry] {

  import com.github.wakfutcp.util.Extensions._

  def read(buf: ByteBuffer) =
    MarketEntry(
      buf.getLong,
      buf.getLong,
      buf.getUTF8_16,
      buf.get,
      buf.getShort,
      buf.getInt,
      buf.get,
      new DateTime(buf.getLong),
      RawInventoryItem.read(buf)
    )
}
