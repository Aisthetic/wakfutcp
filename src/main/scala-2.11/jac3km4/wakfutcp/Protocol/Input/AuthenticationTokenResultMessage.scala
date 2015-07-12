package jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

sealed trait AuthenticationTokenResultMessage extends InputMessage

object AuthenticationTokenResultMessage
  extends InputMessageReader[AuthenticationTokenResultMessage] {

  import jac3km4.wakfutcp.Util.Extensions._

  case class Success(token: String) extends AuthenticationTokenResultMessage

  case class Failure() extends AuthenticationTokenResultMessage

  def read(buf: ByteBuffer) =
    buf.get match {
      case 0 =>
        Success(buf.getUTF8_32)
      case _ =>
        Failure()
    }
}