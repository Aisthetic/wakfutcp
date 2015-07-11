package jac3km4.wakfutcp

import jac3km4.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}
import jac3km4.wakfutcp.WakfuTcpClient.Outbound


trait Messenger {
  def wrap[T <: OutputMessage : OutputMessageWriter](msg: T) =
    Outbound(implicitly[OutputMessageWriter[T]].write(msg))
}
