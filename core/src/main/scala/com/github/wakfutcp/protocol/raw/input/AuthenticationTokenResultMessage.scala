package com.github.wakfutcp.protocol.raw.input

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.{InputMessage, InputMessageReader}
import com.github.wakfutcp.util.Extensions

sealed trait AuthenticationTokenResultMessage extends InputMessage

object AuthenticationTokenResultMessage
  extends InputMessageReader[AuthenticationTokenResultMessage] {

  import Extensions._

  def read(buf: ByteBuffer) =
    buf.get match {
      case 0 ⇒
        Success(buf.getUTF8_32)
      case _ ⇒
        Failure
    }

  case class Success(token: String) extends AuthenticationTokenResultMessage

  case object Failure extends AuthenticationTokenResultMessage

}