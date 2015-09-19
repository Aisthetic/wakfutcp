package com.github

import akka.util.ByteString
import com.github.wakfutcp.protocol.domain.{Character, Proxy, WorldInfo}
import com.github.wakfutcp.protocol.{OutputMessage, OutputMessageWriter}

package object wakfutcp {

  trait Messenger {
    def wrap[T <: OutputMessage : OutputMessageWriter](msg: T) =
      ByteString(implicitly[OutputMessageWriter[T]].write(msg).array)
  }

  trait State

  trait Data

  // client protocol
  final case class LogIn(username: String, password: String)

  final case class ServerList(proxies: Array[Proxy], worldInfos: Array[WorldInfo])

  final case class CharacterList(characters: Array[Character])

  final case class ServerChoice(proxy: Proxy)

  final case class CharacterChoice(character: Character)

  final case class ConnectedToWorld()

  // exceptions
  case class ClientVersionException() extends Exception("Wrong client version")

  case class ConnectionException(msg: String) extends Exception(msg)

  case class AuthenticationException(msg: String) extends Exception(msg)

}
