package com.github.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.{InputMessage, InputMessageReader}

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

  import com.github.wakfutcp.Util.Extensions._

  def read(buf: ByteBuffer) = ActorSpawnMessage(
    buf.get == 1,
    Array.fill(buf.get) {
      SubActor(buf.get, buf.getLong, buf.getByteArray_16)
    }
  )
}