package jac3km4.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

case class RawPet
(
  definitionId: Int,
  name: String,
  colorItemRefId: Int,
  equippedItemRefId: Int,
  health: Int,
  xp: Int,
  fightCounter: Byte,
  fightCounterStartDate: Long,
  lastMealDate: Long,
  lastHungryDate: Long,
  sleepRefItemId: Int,
  sleepDate: Long
  ) extends DataObject

object RawPet extends DataObjectReader[RawPet] {

  import jac3km4.wakfutcp.Util.Extensions._

  def read(buf: ByteBuffer) = {
    RawPet(
      buf.getInt,
      buf.getUTF8_16,
      buf.getInt,
      buf.getInt,
      buf.getInt,
      buf.getInt,
      buf.get,
      buf.getLong,
      buf.getLong,
      buf.getLong,
      buf.getInt,
      buf.getLong
    )
  }
}
