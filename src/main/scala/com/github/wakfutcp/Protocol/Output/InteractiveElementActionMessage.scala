package com.github.wakfutcp.Protocol.Output

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

case class InteractiveElementActionMessage
(
  elementId: Long,
  actionId: Short
  ) extends OutputMessage

object InteractiveElementActionMessage
  extends OutputMessageWriter[InteractiveElementActionMessage] {
  val id = 201

  def write(msg: InteractiveElementActionMessage) = pack(ByteBuffer
    .allocate(12)
    .putLong(msg.elementId)
    .putShort(msg.actionId)
    .array, 3)
}
