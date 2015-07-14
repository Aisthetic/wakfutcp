package com.github.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.Domain.Version
import com.github.wakfutcp.Protocol.{InputMessage, InputMessageReader}

case class ClientVersionResultMessage
(
  success: Boolean,
  required: Version
  ) extends InputMessage

object ClientVersionResultMessage
  extends InputMessageReader[ClientVersionResultMessage] {

  def read(buf: ByteBuffer) =
    ClientVersionResultMessage(
      buf.get == 1,
      Version.read(buf)
    )
}
