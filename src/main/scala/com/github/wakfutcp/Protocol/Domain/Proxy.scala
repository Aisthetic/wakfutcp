package com.github.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

case class Proxy
(
  id: Int,
  name: String,
  community: Int,
  address: String,
  ports: Array[Int],
  order: Byte
  ) extends DataObject

object Proxy extends DataObjectReader[Proxy] {

  import com.github.wakfutcp.Util.Extensions._

  def read(buf: ByteBuffer) = {
    Proxy(
      buf.getInt,
      buf.getUTF8_32,
      buf.getInt,
      buf.getUTF8_32,
      Array.fill(buf.getInt)(buf.getInt),
      buf.get
    )
  }
}