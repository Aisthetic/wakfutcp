package com.github.wakfutcp.protocol.domain

import java.nio.ByteBuffer

import scala.concurrent.duration._

case class RawRentInfo
(
  kind: Int,
  duration: FiniteDuration,
  count: Long
  ) extends DataObject

object RawRentInfo
  extends DataObjectReader[RawRentInfo] {
  def read(buf: ByteBuffer) =
    RawRentInfo(
      buf.getInt,
      buf.getLong.millis,
      buf.getLong
    )
}