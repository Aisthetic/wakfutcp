package jac3km4.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

case class RawItemBind
(
  kind: Byte,
  data: Long
  ) extends DataObject

object RawItemBind
  extends DataObjectReader[RawItemBind] {

  def read(buf: ByteBuffer) =
    RawItemBind(
      buf.get,
      buf.getLong
    )
}