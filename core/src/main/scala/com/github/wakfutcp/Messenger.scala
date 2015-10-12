package com.github.wakfutcp

import akka.util.ByteString
import com.github.wakfutcp.protocol._

trait Messenger {
  def wrap[T <: OutputMessage : OutputMessageWriter](msg: T) =
    ByteString(implicitly[OutputMessageWriter[T]].write(msg).array)
}