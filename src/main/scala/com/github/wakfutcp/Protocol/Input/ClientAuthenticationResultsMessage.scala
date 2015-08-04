package com.github.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.{InputMessage, InputMessageReader}
import com.github.wakfutcp.Util.Extensions
import sun.reflect.generics.reflectiveObjects.NotImplementedException

import scala.concurrent.duration._

sealed trait ClientAuthenticationResultsMessage extends InputMessage

object ClientAuthenticationResultsMessage
  extends InputMessageReader[ClientAuthenticationResultsMessage] {

  import Extensions._

  def read(buf: ByteBuffer) = {
    val resultCode = buf.get
    resultCode match {
      case 0 => Success(buf.getByteArray(buf.getShort))
      case 2 => InvalidLogin
      case 3 => AlreadyConnected
      case 5 => AccountBanned(buf.getInt.minutes)
      case 9 => ServerLocked
      case 10 => LoginServerDown
      case 11 => TooManyConnections
      case 40 => InvalidLogin
      case 42 => InvalidToken
      case _ =>
        throw new NotImplementedException()
    }
  }

  case class AccountBanned
  (
    banDuration: FiniteDuration
    ) extends ClientAuthenticationResultsMessage

  case class Success
  (
    serializedAccountInformation: Array[Byte]
    ) extends ClientAuthenticationResultsMessage

  case object ServerLocked extends ClientAuthenticationResultsMessage

  case object InvalidLogin extends ClientAuthenticationResultsMessage

  case object AlreadyConnected extends ClientAuthenticationResultsMessage

  case object LoginServerDown extends ClientAuthenticationResultsMessage

  case object TooManyConnections extends ClientAuthenticationResultsMessage

  case object InvalidToken extends ClientAuthenticationResultsMessage

}