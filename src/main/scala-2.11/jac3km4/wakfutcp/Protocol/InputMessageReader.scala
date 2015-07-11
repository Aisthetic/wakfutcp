package jac3km4.wakfutcp.Protocol

import java.nio.ByteBuffer

trait InputMessageReader[T <: InputMessage] {
  def read(buf: ByteBuffer): T
}