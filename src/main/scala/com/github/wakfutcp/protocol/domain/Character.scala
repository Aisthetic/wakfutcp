package com.github.wakfutcp.protocol.domain

import java.nio.ByteBuffer

case class Character
(
  id: Long,
  name: String
  ) extends DataObject

object Character extends DataObjectReader[Character] {

  import com.github.wakfutcp.util.Extensions._

  def read(buf: ByteBuffer) = {
    buf.get
    val id = buf.getLong
    buf.get
    buf.getLong
    val name = buf.getUTF8_16
    Character(
      id, name
    )
  }
}