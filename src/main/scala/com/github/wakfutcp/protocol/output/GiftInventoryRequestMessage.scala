package com.github.wakfutcp.protocol.output

import com.github.wakfutcp.protocol.{OutputMessage, OutputMessageWriter}

case class GiftInventoryRequestMessage
(
  lang: String
  ) extends OutputMessage

object GiftInventoryRequestMessage
  extends OutputMessageWriter[GiftInventoryRequestMessage] {
  val id = 13001

  def write(msg: GiftInventoryRequestMessage) =
    pack(msg.lang.getBytes("UTF-8"), 3)
}