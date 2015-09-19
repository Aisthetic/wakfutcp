package com.github.wakfutcp.util

import java.nio.ByteBuffer

object Extensions {

  import scala.language.implicitConversions

  implicit def byteBufferUtil(buf: ByteBuffer): ByteBufferUtil =
    new ByteBufferUtil(buf)

  class ByteBufferUtil(buf: ByteBuffer) {
    def getUTF8_32 = {
      val content = new Array[Byte](buf.getInt)
      buf.get(content)
      new String(content, "UTF-8")
    }

    def getUTF8_16 = {
      val content = new Array[Byte](buf.getShort)
      buf.get(content)
      new String(content, "UTF-8")
    }

    def getByteArray(size: Int) = {
      val data = new Array[Byte](size)
      buf.get(data)
      data
    }

    def getByteArray_32 = {
      val data = new Array[Byte](buf.getInt)
      buf.get(data)
      data
    }

    def getByteArray_16 = {
      val data = new Array[Byte](buf.getShort)
      buf.get(data)
      data
    }
  }

}
