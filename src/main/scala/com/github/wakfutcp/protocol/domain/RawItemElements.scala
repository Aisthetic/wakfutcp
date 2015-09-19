package com.github.wakfutcp.protocol.domain

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