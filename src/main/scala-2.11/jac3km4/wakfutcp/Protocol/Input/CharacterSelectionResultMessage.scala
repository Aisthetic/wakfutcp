package jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

sealed trait CharacterSelectionResultMessage extends InputMessage

object CharacterSelectionResultMessage
  extends InputMessageReader[CharacterSelectionResultMessage] {

  case class Success() extends CharacterSelectionResultMessage

  case class Failure() extends CharacterSelectionResultMessage

  def read(buf: ByteBuffer) =
    buf.get match {
      case 0 =>
        Success()
      case _ =>
        Failure()
    }
}