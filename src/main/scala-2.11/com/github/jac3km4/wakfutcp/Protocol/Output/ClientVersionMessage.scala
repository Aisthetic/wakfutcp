package com.github.jac3km4.wakfutcp.Protocol.Output

import java.nio.ByteBuffer

import com.github.jac3km4.wakfutcp.Protocol.Domain.Version
import com.github.jac3km4.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}

case class ClientVersionMessage
(
  version: Version
  ) extends OutputMessage

object ClientVersionMessage
  extends OutputMessageWriter[ClientVersionMessage] {
  val id = 7

  def write(msg: ClientVersionMessage) = {
    val build = "-1".getBytes("UTF-8")
    val buf = ByteBuffer.allocate(7)
    buf.put(Version.write(msg.version).array)
    buf.put(build.length.toByte)
    buf.put(build)
    pack(buf.array, 0)
  }
}