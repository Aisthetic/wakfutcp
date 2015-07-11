package jac3km4.wakfutcp.Protocol.Domain

import java.nio.ByteBuffer

case class Version
(
  major: Int,
  minor: Int,
  revision: Int
  ) extends DataObject

object Version
  extends DataObjectWriter[Version] with DataObjectReader[Version] {
  def write(ver: Version) = {
    val buf = ByteBuffer.allocate(4)
    buf.put(ver.major.toByte)
    buf.putShort(ver.minor.toShort)
    buf.put(ver.revision.toByte)
    buf
  }

  def read(buf: ByteBuffer) =
    Version(
      buf.get,
      buf.getShort,
      buf.get
    )

  def readWithBuild(buf: ByteBuffer) = {
    val ver = read(buf)
    val len = buf.get
    buf.position(buf.position + len)
    ver
  }
}
