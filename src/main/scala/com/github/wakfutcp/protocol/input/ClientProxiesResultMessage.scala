package com.github.wakfutcp.protocol.input

import java.nio.ByteBuffer

import com.github.wakfutcp.protocol.domain.WorldInfo
import com.github.wakfutcp.protocol.{domain, InputMessage, InputMessageReader}

case class ClientProxiesResultMessage
(
  proxies: Array[domain.Proxy],
  worlds: Array[WorldInfo]
  ) extends InputMessage

object ClientProxiesResultMessage extends InputMessageReader[ClientProxiesResultMessage] {
  def read(buf: ByteBuffer) = {
    ClientProxiesResultMessage(
      Array.fill(buf.getInt)(domain.Proxy.read(buf)),
      Array.fill(buf.getInt)(WorldInfo.read(buf))
    )
  }
}
