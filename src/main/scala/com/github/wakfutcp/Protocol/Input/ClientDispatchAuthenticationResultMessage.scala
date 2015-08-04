package com.github.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.Input.ClientDispatchAuthenticationResultMessage.Result
import com.github.wakfutcp.Protocol.{InputMessage, InputMessageReader}

case class ClientDispatchAuthenticationResultMessage
(
  result: Result,
  activateSteamLinkHit: Boolean,
  community: Option[Int]
  ) extends InputMessage

object ClientDispatchAuthenticationResultMessage
  extends InputMessageReader[ClientDispatchAuthenticationResultMessage] {

  def read(buf: ByteBuffer) =
    ClientDispatchAuthenticationResultMessage(
      buf.get match {
        case 0 => Success
        case 7 => InternalError
        case 5 => Banned
        case 2 => InvalidLogin
        case 25 => ForbiddenCommunity
        case _ => Unknown
      },
      buf.get == 1,
      if (buf.get == 1) Some(buf.getInt) else None
    )

  sealed trait Result

  case object Success extends Result

  case object InternalError extends Result

  case object Banned extends Result

  case object InvalidLogin extends Result

  case object ForbiddenCommunity extends Result

  case object Unknown extends Result

}