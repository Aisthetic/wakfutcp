package jac3km4.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

case class RawItemXp
(
  definitionId: Int,
  xp: Long
  ) extends DataObject

object RawItemXp extends DataObjectReader[RawItemXp] {
  def read(buf: ByteBuffer) =
    RawItemXp(
      buf.getInt,
      buf.getLong
    )
}