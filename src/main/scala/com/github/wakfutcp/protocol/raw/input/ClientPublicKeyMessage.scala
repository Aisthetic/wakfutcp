package com.github.wakfutcp.protocol.raw.input

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.{InputMessage, InputMessageReader}
import com.github.wakfutcp.util.Extensions

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
      buf.getByteArray(buf.limit - buf.position)
    )
  }
}