package com.github.wakfutcp.protocol

import java.nio.ByteBuffer

trait InputMessageReader[T <: InputMessage] {
  def read(buf: ByteBuffer): T
}