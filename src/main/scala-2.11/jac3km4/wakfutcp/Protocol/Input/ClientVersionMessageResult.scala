package jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.Domain.Version
import jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

case class ClientVersionMessageResult
(
  success: Boolean,
  required: Version
  ) extends InputMessage

object ClientVersionMessageResult
  extends InputMessageReader[ClientVersionMessageResult] {

  def read(buf: ByteBuffer) =
    ClientVersionMessageResult(
      buf.get == 1,
      Version.read(buf)
    )
}
