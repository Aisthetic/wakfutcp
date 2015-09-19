package com.github.wakfutcp.protocol.input

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.{InputMessage, InputMessageReader}
import com.github.wakfutcp.util.Extensions

case class ClientIpMessage(bytes: Array[Byte]) extends InputMessage

object ClientIpMessage
  extends InputMessageReader[ClientIpMessage] {

  import Extensions._

  def read(buf: ByteBuffer) = {
    ClientIpMessage(
      buf.getByteArray(4)
    )
  }
}