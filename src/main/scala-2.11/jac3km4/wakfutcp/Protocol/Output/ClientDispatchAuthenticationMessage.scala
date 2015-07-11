package jac3km4.wakfutcp.Protocol.Output

import java.nio.ByteBuffer
import javax.crypto.Cipher

import jac3km4.wakfutcp.Protocol.{OutputMessage, OutputMessageWriter}


case class ClientDispatchAuthenticationMessage
(
  encryptedCredentials: Array[Byte]
  ) extends OutputMessage

object ClientDispatchAuthenticationMessage
  extends OutputMessageWriter[ClientDispatchAuthenticationMessage] {
  val id = 1026

  def create(login: String, password: String, salt: Long, cipher: Cipher) = {
    val buf = ByteBuffer.allocate(10 + login.length + password.length)
    buf.putLong(salt)
    buf.put(login.length.toByte)
    buf.put(login.getBytes("UTF-8"))
    buf.put(password.length.toByte)
    buf.put(password.getBytes("UTF-8"))
    ClientDispatchAuthenticationMessage(
      cipher.doFinal(buf.array)
    )
  }

  def write(msg: ClientDispatchAuthenticationMessage) = {
    val buf = ByteBuffer.allocate(4 + msg.encryptedCredentials.length)
    buf.putInt(msg.encryptedCredentials.length)
    buf.put(msg.encryptedCredentials)
    pack(buf.array, 8)
  }
}