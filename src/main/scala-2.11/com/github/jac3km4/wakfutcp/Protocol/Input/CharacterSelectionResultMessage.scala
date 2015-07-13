package com.github.jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

sealed trait CharacterSelectionResultMessage extends InputMessage

object CharacterSelectionResultMessage
  extends InputMessageReader[CharacterSelectionResultMessage] {

  def read(buf: ByteBuffer) =
    buf.get match {
      case 0 =>
        Success()
      case _ =>
        Failure()
    }

  case class Success() extends CharacterSelectionResultMessage

  case class Failure() extends CharacterSelectionResultMessage

}