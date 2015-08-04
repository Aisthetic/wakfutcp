package com.github.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.{InputMessage, InputMessageReader}

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