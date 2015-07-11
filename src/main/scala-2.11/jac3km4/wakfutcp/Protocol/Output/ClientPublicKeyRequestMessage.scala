package jac3km4.wakfutcp.Protocol.Output

import jac3km4.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}


case class ClientPublicKeyRequestMessage
(
  serverId: Byte
  ) extends OutputMessage

object ClientPublicKeyRequestMessage
  extends OutputMessageWriter[ClientPublicKeyRequestMessage] {
  val id = 1033

  def write(msg: ClientPublicKeyRequestMessage) =
    pack(Array.empty[Byte], msg.serverId)
}