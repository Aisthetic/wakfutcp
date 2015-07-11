package jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

case class ClientDispatchAuthenticationResultMessage
(
  resultCode: Byte,
  activateSteamLinkHit: Boolean,
  community: Option[Int]
  ) extends InputMessage

object ClientDispatchAuthenticationResultMessage
  extends InputMessageReader[ClientDispatchAuthenticationResultMessage] {

  def read(buf: ByteBuffer) =
    ClientDispatchAuthenticationResultMessage(
      buf.get,
      buf.get == 1,
      if (buf.get == 1) Some(buf.getInt) else None
    )
}