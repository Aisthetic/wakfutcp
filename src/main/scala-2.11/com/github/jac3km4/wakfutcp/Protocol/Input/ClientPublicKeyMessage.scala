package com.github.jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}
import com.github.jac3km4.wakfutcp.Util.Extensions

case class ClientPublicKeyMessage
(
  salt: Long,
  publicKey: Array[Byte]
  ) extends InputMessage


object ClientPublicKeyMessage
  extends InputMessageReader[ClientPublicKeyMessage] {

  import Extensions._

  def read(buf: ByteBuffer) = {
    ClientPublicKeyMessage(
      buf.getLong,
      buf.getByteArray(buf.limit - 10)
    )
  }
}