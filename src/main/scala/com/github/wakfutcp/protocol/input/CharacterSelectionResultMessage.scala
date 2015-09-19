package com.github.wakfutcp.protocol.input

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.{InputMessage, InputMessageReader}

sealed trait CharacterSelectionResultMessage extends InputMessage

object CharacterSelectionResultMessage
  extends InputMessageReader[CharacterSelectionResultMessage] {

  def read(buf: ByteBuffer) =
    buf.get match {
      case 0 =>
        Success
      case _ =>
        Failure
    }

  case object Success extends CharacterSelectionResultMessage

  case object Failure extends CharacterSelectionResultMessage

}