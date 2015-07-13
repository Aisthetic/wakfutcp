package com.github.jac3km4.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

case class RawItemElements
(
  damageElements: Byte,
  resistElements: Byte
  ) extends DataObject

object RawItemElements
  extends DataObjectReader[RawItemElements] {
  def read(buf: ByteBuffer) =
    RawItemElements(
      buf.get,
      buf.get
    )
}