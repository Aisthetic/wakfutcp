package com.github.wakfutcp.protocol.domain

import java.nio.ByteBuffer

case class Version
(
  major: Int,
  minor: Int,
  revision: Int
  ) extends DataObject

object Version
  extends DataObjectWriter[Version] with DataObjectReader[Version] {
  def write(ver: Version) = ByteBuffer
    .allocate(4)
    .put(ver.major.toByte)
    .putShort(ver.minor.toShort)
    .put(ver.revision.toByte)

  def read(buf: ByteBuffer) =
    Version(
      buf.get.toInt,
      buf.getShort.toInt,
      buf.get.toInt
    )

  def readWithBuild(buf: ByteBuffer) = {
    val ver = read(buf)
    val len = buf.get
    buf.position(buf.position + len)
    ver
  }
}
