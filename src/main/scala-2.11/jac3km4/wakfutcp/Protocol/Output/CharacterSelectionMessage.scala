package jac3km4.wakfutcp.Protocol.Output

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

case class CharacterSelectionMessage
(
  characterId: Long,
  characterName: String
  ) extends OutputMessage

object CharacterSelectionMessage
  extends OutputMessageWriter[CharacterSelectionMessage] {
  val id = 2049

  def write(msg: CharacterSelectionMessage) = {
    val buf = ByteBuffer.allocate(12 + msg.characterName.length)
    buf.putLong(msg.characterId)
    buf.putInt(msg.characterName.length)
    buf.put(msg.characterName.getBytes("UTF-8"))
    pack(buf.array, 2)
  }
}