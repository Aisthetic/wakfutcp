package jac3km4.wakfutcp.Protocol.Output

import jac3km4.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

case class EndConnectionMessage() extends OutputMessage


object EndConnectionMessage
  extends OutputMessageWriter[EndConnectionMessage] {
  val id = 1

  def write(msg: EndConnectionMessage) = {
    pack(Array.empty[Byte], 0)
  }
}