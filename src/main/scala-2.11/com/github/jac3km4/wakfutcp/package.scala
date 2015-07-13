package com.github.jac3km4

import akka.util.ByteString
import com.github.jac3km4.wakfutcp.Protocol.Domain.WorldInfo
import com.github.jac3km4.wakfutcp.Protocol.{Domain, OutputMessage, OutputMessageWriter}

package object wakfutcp {

  trait Messenger {
    def wrap[T <: OutputMessage : OutputMessageWriter](msg: T) =
      ByteString(implicitly[OutputMessageWriter[T]].write(msg).array)
  }

  // client protocol
  case class ServerList(proxies: Array[Domain.Proxy], worldInfos: Array[WorldInfo])

  case class CharacterList(characters: Array[Domain.Character])

  case class ServerChoice(proxy: Domain.Proxy)

  case class CharacterChoice(character: Domain.Character)

  case class ConnectedToWorld()

  // exceptions
  case class ClientVersionException() extends Exception("Wrong client version")

  case class ConnectionException(msg: String) extends Exception(msg)

  case class AuthenticationException(msg: String) extends Exception(msg)

}
