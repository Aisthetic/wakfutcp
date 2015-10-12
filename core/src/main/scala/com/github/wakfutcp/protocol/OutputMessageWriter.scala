package com.github.wakfutcp.protocol

import java.nio.ByteBuffer

trait OutputMessageWriter[T <: OutputMessage] {
  def id: Int

  protected def pack(data: Array[Byte], arch: Byte): ByteBuffer = {
    val len = data.length + 5
    ByteBuffer.allocate(len)
      .putShort(len.toShort)
      .put(arch)
      .putShort(id.toShort)
      .put(data)
  }

  def write(msg: T): ByteBuffer
}