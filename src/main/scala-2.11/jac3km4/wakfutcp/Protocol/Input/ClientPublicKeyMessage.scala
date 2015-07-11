package jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

case class ClientPublicKeyMessage
(
  salt: Long,
  publicKey: Array[Byte]
  ) extends InputMessage


object ClientPublicKeyMessage
  extends InputMessageReader[ClientPublicKeyMessage] {

  import jac3km4.wakfutcp.Util.Extensions._

  def read(buf: ByteBuffer) = {
    ClientPublicKeyMessage(
      buf.getLong,
      buf.getByteArray(buf.limit - 10)
    )
  }
}