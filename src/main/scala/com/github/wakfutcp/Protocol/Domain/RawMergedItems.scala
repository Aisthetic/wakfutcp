package com.github.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

case class RawMergedItems
(
  version: Int,
  items: Array[Byte]
  ) extends DataObject

object RawMergedItems
  extends DataObjectReader[RawMergedItems] {

  import com.github.wakfutcp.Util.Extensions._

  def read(buf: ByteBuffer) =
    RawMergedItems(
      buf.getInt,
      buf.getByteArray_16
    )
}
