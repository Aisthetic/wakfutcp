package jac3km4.wakfutcp.Protocol.Output

import jac3km4.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

case class ClientProxiesRequestMessage() extends OutputMessage

object ClientProxiesRequestMessage
  extends OutputMessageWriter[ClientProxiesRequestMessage] {
  val id = 1035

  def write(msg: ClientProxiesRequestMessage) =
    pack(Array.empty[Byte], 8)
}