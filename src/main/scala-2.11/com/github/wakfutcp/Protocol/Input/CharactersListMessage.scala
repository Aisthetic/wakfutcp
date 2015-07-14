package com.github.wakfutcp.Protocol.Input

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.Domain.Character
import com.github.wakfutcp.Protocol.{InputMessage, InputMessageReader}


case class CharactersListMessage(characters: Array[Character]) extends InputMessage

object CharactersListMessage
  extends InputMessageReader[CharactersListMessage] {

  import com.github.wakfutcp.Util.Extensions._

  def read(buf: ByteBuffer) = {
    val chars = Array.fill(buf.get) {
      Character.read(ByteBuffer.wrap(buf.getByteArray(buf.getShort)))
    }
    CharactersListMessage(chars)
  }
}
