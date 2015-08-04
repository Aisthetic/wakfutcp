package com.github.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.{InputMessage, InputMessageReader}
import com.github.wakfutcp.Util.Extensions

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