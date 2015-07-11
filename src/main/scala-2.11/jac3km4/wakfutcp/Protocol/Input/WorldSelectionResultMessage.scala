package jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

case class WorldSelectionResultMessage
(
  errorCode: Byte
  ) extends InputMessage

object WorldSelectionResultMessage
  extends InputMessageReader[WorldSelectionResultMessage] {

  def read(buf: ByteBuffer) =
    WorldSelectionResultMessage(buf.get)
}