package jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

case class CharacterSelectionResultMessage
(
  errorCode: Byte
  ) extends InputMessage

object CharacterSelectionResultMessage
  extends InputMessageReader[CharacterSelectionResultMessage] {
  def read(buf: ByteBuffer) =
    CharacterSelectionResultMessage(buf.get)
}