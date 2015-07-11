package jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

case class ClientIpMessage(bytes: Array[Byte]) extends InputMessage

object ClientIpMessage
  extends InputMessageReader[ClientIpMessage] {

  import jac3km4.wakfutcp.Util.Extensions._

  def read(buf: ByteBuffer) = {
    ClientIpMessage(
      buf.getByteArray(4)
    )
  }
}