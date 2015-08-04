package com.github.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

case class RawGem
(
  position: Byte,
  referenceId: Int
  ) extends DataObject

object RawGem extends DataObjectReader[RawGem] {
  def read(buf: ByteBuffer) =
    RawGem(
      buf.get,
      buf.getInt
    )
}