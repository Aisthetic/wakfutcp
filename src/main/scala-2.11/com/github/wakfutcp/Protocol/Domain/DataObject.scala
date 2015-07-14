package com.github.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

trait DataObject

trait DataObjectReader[T <: DataObject] {
  def read(buf: ByteBuffer): T
}

trait DataObjectWriter[T <: DataObject] {
  def write(obj: T): ByteBuffer
}