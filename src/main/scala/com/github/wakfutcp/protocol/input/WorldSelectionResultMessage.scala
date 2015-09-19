package com.github.wakfutcp.protocol.input

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.{InputMessage, InputMessageReader}

sealed trait WorldSelectionResultMessage extends InputMessage

object WorldSelectionResultMessage
  extends InputMessageReader[WorldSelectionResultMessage] {

  def read(buf: ByteBuffer) =
    buf.get match {
      case 0 | 9 =>
        Success
      case _ =>
        Failure
    }

  case object Success extends WorldSelectionResultMessage

  case object Failure extends WorldSelectionResultMessage

}