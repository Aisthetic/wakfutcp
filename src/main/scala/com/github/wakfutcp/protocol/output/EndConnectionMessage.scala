package com.github.wakfutcp.protocol.output

import com.github.wakfutcp.protocol.{OutputMessage, OutputMessageWriter}

case class EndConnectionMessage() extends OutputMessage

object EndConnectionMessage
  extends OutputMessageWriter[EndConnectionMessage] {
  val id = 1

  def write(msg: EndConnectionMessage) = {
    pack(Array.empty[Byte], 0)
  }
}