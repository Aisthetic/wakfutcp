package jac3km4.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

case class RawRentInfo
(
  kind: Int,
  duration: Long,
  count: Long
  ) extends DataObject

object RawRentInfo
  extends DataObjectReader[RawRentInfo] {
  def read(buf: ByteBuffer) =
    RawRentInfo(
      buf.getInt,
      buf.getLong,
      buf.getLong
    )
}