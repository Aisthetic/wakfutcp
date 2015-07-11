package jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

case class ForcedDisconnectionReasonMessage
(
  reason: Byte
  ) extends InputMessage

object ForcedDisconnectionReasonMessage
  extends InputMessageReader[ForcedDisconnectionReasonMessage] {

  def read(buf: ByteBuffer) =
    ForcedDisconnectionReasonMessage(buf.get)
}
