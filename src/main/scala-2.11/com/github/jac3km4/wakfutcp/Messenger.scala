package com.github.jac3km4.wakfutcp

import com.github.jac3km4.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}
import com.github.jac3km4.wakfutcp.WakfuTcpClient.Outbound

trait Messenger {
  def wrap[T <: OutputMessage : OutputMessageWriter](msg: T) =
    Outbound(implicitly[OutputMessageWriter[T]].write(msg))
}
