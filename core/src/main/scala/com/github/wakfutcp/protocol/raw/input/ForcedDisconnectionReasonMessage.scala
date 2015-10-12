package com.github.wakfutcp.protocol.raw.input

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.{InputMessage, InputMessageReader}
import sun.reflect.generics.reflectiveObjects.NotImplementedException

sealed trait ForcedDisconnectionReasonMessage extends InputMessage

object ForcedDisconnectionReasonMessage
  extends InputMessageReader[ForcedDisconnectionReasonMessage] {

  def read(buf: ByteBuffer) =
    buf.get match {
      case 1 ⇒ Spam
      case 2 ⇒ Timeout
      case 3 ⇒ KickedByReconnect
      case 4 ⇒ KickedByAdmin
      case 5 ⇒ BannedByAdmin
      case 7 ⇒ SessionDestroyed
      case 8 ⇒ ServerDoesNotAnswer
      case 9 ⇒ ServerShutdown
      case 12 ⇒ ServerError
      case 14 ⇒ SynchronizationError
      case 16 ⇒ ServerFull
      case 17 ⇒ TimedSessionEnd
      case 18 ⇒ AccountBanned
      case _ =>
        throw new NotImplementedException()
    }

  case object Spam extends ForcedDisconnectionReasonMessage

  case object Timeout extends ForcedDisconnectionReasonMessage

  case object KickedByReconnect extends ForcedDisconnectionReasonMessage

  case object KickedByAdmin extends ForcedDisconnectionReasonMessage

  case object AccountBanned extends ForcedDisconnectionReasonMessage

  case object BannedByAdmin extends ForcedDisconnectionReasonMessage

  case object SessionDestroyed extends ForcedDisconnectionReasonMessage

  case object ServerDoesNotAnswer extends ForcedDisconnectionReasonMessage

  case object ServerShutdown extends ForcedDisconnectionReasonMessage

  case object ServerError extends ForcedDisconnectionReasonMessage

  case object SynchronizationError extends ForcedDisconnectionReasonMessage

  case object TimedSessionEnd extends ForcedDisconnectionReasonMessage

  case object ServerFull extends ForcedDisconnectionReasonMessage

}
