package com.github.jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

sealed trait WorldSelectionResultMessage extends InputMessage

object WorldSelectionResultMessage
  extends InputMessageReader[WorldSelectionResultMessage] {

  def read(buf: ByteBuffer) =
    buf.get match {
      case 0 | 9 =>
        Success()
      case _ =>
        Failure()
    }

  case class Success() extends WorldSelectionResultMessage

  case class Failure() extends WorldSelectionResultMessage

}