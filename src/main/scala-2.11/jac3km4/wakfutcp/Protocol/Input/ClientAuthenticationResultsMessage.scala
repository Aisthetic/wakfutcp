package jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

import scala.concurrent.duration._

sealed trait ClientAuthenticationResultsMessage extends InputMessage

object ClientAuthenticationResultsMessage
  extends InputMessageReader[ClientAuthenticationResultsMessage] {

  import jac3km4.wakfutcp.Util.Extensions._

  case class AccountBanned
  (
    banDuration: FiniteDuration
    ) extends ClientAuthenticationResultsMessage

  case class SuccessfulAuthentication
  (
    serializedAccountInformation: Array[Byte]
    ) extends ClientAuthenticationResultsMessage

  case class ServerLocked() extends ClientAuthenticationResultsMessage

  case class InvalidLogin() extends ClientAuthenticationResultsMessage

  case class AlreadyConnected() extends ClientAuthenticationResultsMessage

  case class LoginServerDown() extends ClientAuthenticationResultsMessage

  case class TooManyConnections() extends ClientAuthenticationResultsMessage

  case class InvalidToken() extends ClientAuthenticationResultsMessage

  def read(buf: ByteBuffer) = {
    val resultCode = buf.get
    resultCode match {
      case 0 => SuccessfulAuthentication(buf.getByteArray(buf.getShort))
      case 2 => InvalidLogin()
      case 3 => AlreadyConnected()
      case 5 => AccountBanned(buf.getInt.minutes)
      case 9 => ServerLocked()
      case 10 => LoginServerDown()
      case 11 => TooManyConnections()
      case 40 => InvalidLogin()
      case 42 => InvalidToken()
    }
  }
}