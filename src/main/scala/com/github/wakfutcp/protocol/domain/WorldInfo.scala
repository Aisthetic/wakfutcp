package com.github.wakfutcp.protocol.domain

import java.nio.ByteBuffer

case class ConfigProperty
(
  property: Short,
  value: String
  ) extends DataObject

case class WorldInfo
(
  serverId: Int,
  version: Version,
  config: Array[ConfigProperty],
  playerCount: Int,
  playerLimit: Int,
  locked: Boolean
  ) extends DataObject

object WorldInfo extends DataObjectReader[WorldInfo] {

  import com.github.wakfutcp.util.Extensions._

  def read(buf: ByteBuffer) = {
    WorldInfo(
      buf.getInt,
      Version.readWithBuild(ByteBuffer.wrap(buf.getByteArray_32)),
      readConfigProperties(ByteBuffer.wrap(buf.getByteArray_32)),
      buf.getInt,
      buf.getInt,
      buf.get != 0
    )
  }

  def readConfigProperties(buf: ByteBuffer): Array[ConfigProperty] =
    Array.fill(buf.getInt)(ConfigProperty(buf.getShort, buf.getUTF8_32))
}