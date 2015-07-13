package com.github.jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

case class ClientIpMessage(bytes: Array[Byte]) extends InputMessage

object ClientIpMessage
  extends InputMessageReader[ClientIpMessage] {

  import com.github.jac3km4.wakfutcp.Util.Extensions._

  def read(buf: ByteBuffer) = {
    ClientIpMessage(
      buf.getByteArray(4)
    )
  }
}