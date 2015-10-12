package com.github.wakfutcp

object Exceptions {
  case class ClientVersionException() extends Exception("Wrong client version")

  case class ConnectionException(msg: String) extends Exception(msg)

  case class AuthenticationException(msg: String) extends Exception(msg)
}
