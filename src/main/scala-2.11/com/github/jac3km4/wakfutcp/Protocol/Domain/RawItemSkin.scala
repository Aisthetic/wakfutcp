package com.github.jac3km4.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

case class RawItemSkin
(
  skinRef: Int
  ) extends DataObject

object RawItemSkin
  extends DataObjectReader[RawItemSkin] {
  def read(buf: ByteBuffer) =
    RawItemSkin(buf.getInt)
}