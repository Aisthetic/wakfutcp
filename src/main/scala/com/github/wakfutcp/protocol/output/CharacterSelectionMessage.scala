package com.github.wakfutcp.protocol.output

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.{OutputMessage, OutputMessageWriter}

case class CharacterSelectionMessage
(
  characterId: Long,
  characterName: String
  ) extends OutputMessage

object CharacterSelectionMessage
  extends OutputMessageWriter[CharacterSelectionMessage] {
  val id = 2049

  def write(msg: CharacterSelectionMessage) = pack(ByteBuffer
    .allocate(12 + msg.characterName.length)
    .putLong(msg.characterId)
    .putInt(msg.characterName.length)
    .put(msg.characterName.getBytes("UTF-8"))
    .array, 2)
}