package jac3km4.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

case class MarketEntry
(
  id: Long,
  sellerId: Long,
  sellerName: String,
  packTypeId: Byte,
  packNumber: Short,
  packPrice: Int,
  durationId: Byte,
  releaseDate: Long,
  rawItem: RawInventoryItem
  ) extends DataObject

object MarketEntry extends DataObjectReader[MarketEntry] {

  import jac3km4.wakfutcp.Util.Extensions._

  def read(buf: ByteBuffer) =
    MarketEntry(
      buf.getLong,
      buf.getLong,
      buf.getUTF8_16,
      buf.get,
      buf.getShort,
      buf.getInt,
      buf.get,
      buf.getLong,
      RawInventoryItem.read(buf)
    )
}
