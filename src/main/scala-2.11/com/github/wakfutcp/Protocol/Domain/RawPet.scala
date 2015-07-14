package com.github.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

import com.github.nscala_time.time.Imports._

case class RawPet
(
  definitionId: Int,
  name: String,
  colorItemRefId: Int,
  equippedItemRefId: Int,
  health: Int,
  xp: Int,
  fightCounter: Byte,
  fightCounterStartDate: DateTime,
  lastMealDate: DateTime,
  lastHungryDate: DateTime,
  sleepRefItemId: Int,
  sleepDate: DateTime
  ) extends DataObject

object RawPet extends DataObjectReader[RawPet] {

  import com.github.wakfutcp.Util.Extensions._

  def read(buf: ByteBuffer) = {
    RawPet(
      buf.getInt,
      buf.getUTF8_16,
      buf.getInt,
      buf.getInt,
      buf.getInt,
      buf.getInt,
      buf.get,
      new DateTime(buf.getLong),
      new DateTime(buf.getLong),
      new DateTime(buf.getLong),
      buf.getInt,
      new DateTime(buf.getLong)
    )
  }
}
