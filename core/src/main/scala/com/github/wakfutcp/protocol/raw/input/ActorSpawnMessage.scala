package com.github.wakfutcp.protocol.raw.input

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.{InputMessage, InputMessageReader}

case class ActorSpawnMessage
(
  myFightSpawn: Boolean,
  subActors: Array[SubActor]
  ) extends InputMessage

case class SubActor
(
  opponentType: Byte,
  id: Long,
  serialized: Array[Byte]
  )

object ActorSpawnMessage extends InputMessageReader[ActorSpawnMessage] {

  import com.github.wakfutcp.util.Extensions._

  def read(buf: ByteBuffer) = ActorSpawnMessage(
    buf.get == 1,
    Array.fill(buf.get.toInt) {
      SubActor(buf.get, buf.getLong, buf.getByteArray_16)
    }
  )
}