package com.github.wakfutcp.protocol.output

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.domain.Version
import com.github.wakfutcp.protocol.{OutputMessage, OutputMessageWriter}

case class ClientVersionMessage
(
  version: Version
  ) extends OutputMessage

object ClientVersionMessage
  extends OutputMessageWriter[ClientVersionMessage] {
  val id = 7

  def write(msg: ClientVersionMessage) = {
    val build = "-1".getBytes("UTF-8")
    pack(ByteBuffer.allocate(7)
      .put(Version.write(msg.version).array)
      .put(build.length.toByte)
      .put(build)
      .array, 0)
  }
}