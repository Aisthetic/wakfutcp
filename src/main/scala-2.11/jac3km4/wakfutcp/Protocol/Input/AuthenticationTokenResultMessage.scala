package jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

case class AuthenticationTokenResultMessage
(
  resultCode: Byte,
  token: String
  ) extends InputMessage

object AuthenticationTokenResultMessage
  extends InputMessageReader[AuthenticationTokenResultMessage] {

  import jac3km4.wakfutcp.Util.Extensions._

  def read(buf: ByteBuffer) =
    AuthenticationTokenResultMessage(
      buf.get,
      buf.getUTF8_32
    )
}