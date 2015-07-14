package com.github.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.{InputMessage, InputMessageReader}
import sun.reflect.generics.reflectiveObjects.NotImplementedException

sealed trait ForcedDisconnectionReasonMessage extends InputMessage

object ForcedDisconnectionReasonMessage
  extends InputMessageReader[ForcedDisconnectionReasonMessage] {

  def read(buf: ByteBuffer) =
    buf.get match {
      case 1 => Spam()
      case 2 => Timeout()
      case 3 => KickedByReconnect()
      case 4 => KickedByAdmin()
      case 5 => BannedByAdmin()
      case 7 => SessionDestroyed()
      case 8 => ServerDoesNotAnswer()
      case 9 => ServerShutdown()
      case 12 => ServerError()
      case 14 => SynchronizationError()
      case 16 => ServerFull()
      case 17 => TimedSessionEnd()
      case 18 => AccountBanned()
      case _ =>
        throw new NotImplementedException()
    }

  case class Spam() extends ForcedDisconnectionReasonMessage

  case class Timeout() extends ForcedDisconnectionReasonMessage

  case class KickedByReconnect() extends ForcedDisconnectionReasonMessage

  case class KickedByAdmin() extends ForcedDisconnectionReasonMessage

  case class AccountBanned() extends ForcedDisconnectionReasonMessage

  case class BannedByAdmin() extends ForcedDisconnectionReasonMessage

  case class SessionDestroyed() extends ForcedDisconnectionReasonMessage

  case class ServerDoesNotAnswer() extends ForcedDisconnectionReasonMessage

  case class ServerShutdown() extends ForcedDisconnectionReasonMessage

  case class ServerError() extends ForcedDisconnectionReasonMessage

  case class SynchronizationError() extends ForcedDisconnectionReasonMessage

  case class TimedSessionEnd() extends ForcedDisconnectionReasonMessage

  case class ServerFull() extends ForcedDisconnectionReasonMessage

}
