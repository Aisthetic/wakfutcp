package com.github.wakfutcp.Protocol

import java.nio.ByteBuffer

trait InputMessageReader[T <: InputMessage] {
  def read(buf: ByteBuffer): T
}