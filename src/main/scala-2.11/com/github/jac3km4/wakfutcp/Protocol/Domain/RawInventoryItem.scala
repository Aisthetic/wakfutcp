package com.github.jac3km4.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

case class RawInventoryItem
(
  uniqueId: Long,
  refId: Int,
  quantity: Short,
  timestamp: Option[Long],
  rawPet: Option[RawPet],
  xp: Option[RawItemXp],
  gems: Option[Array[RawGem]],
  rentInfo: Option[RawRentInfo],
  companionInfo: Option[RawCompanionInfo],
  bind: Option[RawItemBind],
  elements: Option[RawItemElements],
  mergedItems: Option[RawMergedItems],
  itemSkin: Option[RawItemSkin]
  ) extends DataObject

object RawInventoryItem extends DataObjectReader[RawInventoryItem] {
  def read(buf: ByteBuffer) =
    RawInventoryItem(
      buf.getLong,
      buf.getInt,
      buf.getShort,
      if (buf.get == 1) Some(buf.getLong) else None,
      if (buf.get == 1) Some(RawPet.read(buf)) else None,
      if (buf.get == 1) Some(RawItemXp.read(buf)) else None,
      if (buf.get == 1) Some(Array.fill(buf.getShort)(RawGem.read(buf))) else None,
      if (buf.get == 1) Some(RawRentInfo.read(buf)) else None,
      if (buf.get == 1) Some(RawCompanionInfo.read(buf)) else None,
      if (buf.get == 1) Some(RawItemBind.read(buf)) else None,
      if (buf.get == 1) Some(RawItemElements.read(buf)) else None,
      if (buf.get == 1) Some(RawMergedItems.read(buf)) else None,
      if (buf.get == 1) Some(RawItemSkin.read(buf)) else None
    )
}