package com.github.jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.jac3km4.wakfutcp.Protocol.Domain.{Proxy, WorldInfo}
import com.github.jac3km4.wakfutcp.Protocol.{InputMessage, InputMessageReader}

case class ClientProxiesResultMessage
(
  proxies: Array[Proxy],
  worlds: Array[WorldInfo]
  ) extends InputMessage

object ClientProxiesResultMessage extends InputMessageReader[ClientProxiesResultMessage] {
  def read(buf: ByteBuffer) = {
    ClientProxiesResultMessage(
      Array.fill(buf.getInt)(Proxy.read(buf)),
      Array.fill(buf.getInt)(WorldInfo.read(buf))
    )
  }
}
