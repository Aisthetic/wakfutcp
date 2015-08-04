package com.github.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

case class RawCompanionInfo
(
  xp: Long
  ) extends DataObject

object RawCompanionInfo
  extends DataObjectReader[RawCompanionInfo] {
  def read(buf: ByteBuffer) =
    RawCompanionInfo(buf.getLong)
}
