package com.github.wakfutcp.protocol.input

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.domain.Version
import com.github.wakfutcp.protocol.{InputMessage, InputMessageReader}

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
