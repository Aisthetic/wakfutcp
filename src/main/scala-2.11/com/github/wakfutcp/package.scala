package com.github

import akka.util.ByteString
import com.github.wakfutcp.Protocol.Domain.{Character, Proxy, WorldInfo}
import com.github.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

package object wakfutcp {

  trait Messenger {
    def wrap[T <: OutputMessage : OutputMessageWriter](msg: T) =
      ByteString(implicitly[OutputMessageWriter[T]].write(msg).array)
  }

  // client protocol
  case class ServerList(proxies: Array[Proxy], worldInfos: Array[WorldInfo])

  case class CharacterList(characters: Array[Character])

  case class ServerChoice(proxy: Proxy)

  case class CharacterChoice(character: Character)

  case class ConnectedToWorld()

  // exceptions
  case class ClientVersionException() extends Exception("Wrong client version")

  case class ConnectionException(msg: String) extends Exception(msg)

  case class AuthenticationException(msg: String) extends Exception(msg)

}
