package com.github.jac3km4.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.jac3km4.wakfutcp.Protocol.{Domain, InputMessage, InputMessageReader}

case class CharactersListMessage(characters: Array[Domain.Character]) extends InputMessage

object CharactersListMessage
  extends InputMessageReader[CharactersListMessage] {

  import com.github.jac3km4.wakfutcp.Util.Extensions._

  def read(buf: ByteBuffer) = {
    val chars = Array.fill(buf.get) {
      Domain.Character.read(ByteBuffer.wrap(buf.getByteArray(buf.getShort)))
    }
    CharactersListMessage(chars)
  }
}
