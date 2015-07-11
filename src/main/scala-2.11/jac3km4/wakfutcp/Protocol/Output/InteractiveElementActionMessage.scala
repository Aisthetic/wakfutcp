package jac3km4.wakfutcp.Protocol.Output

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

case class InteractiveElementActionMessage
(
  elementId: Long,
  actionId: Short
  ) extends OutputMessage

object InteractiveElementActionMessage
  extends OutputMessageWriter[InteractiveElementActionMessage] {
  val id = 201

  def write(msg: InteractiveElementActionMessage) = {
    val buf = ByteBuffer.allocate(12)
    buf.putLong(msg.elementId)
    buf.putShort(msg.actionId)
    pack(buf.array, 3)
  }
}
