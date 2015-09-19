package com.github.wakfutcp.protocol.domain

import java.nio.ByteBuffer

case class Position
(
  x: Int,
  y: Int,
  z: Short
  ) extends DataObject

object Position extends DataObjectWriter[Position] with DataObjectReader[Position] {
  def read(buf: ByteBuffer) = Position(
    buf.getInt,
    buf.getInt,
    buf.getShort
  )

  def write(obj: Position) = ByteBuffer
    .allocate(10)
    .putInt(obj.x)
    .putInt(obj.x)
    .putShort(obj.z)
}
