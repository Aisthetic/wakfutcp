package com.github.wakfutcp.protocol.domain

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