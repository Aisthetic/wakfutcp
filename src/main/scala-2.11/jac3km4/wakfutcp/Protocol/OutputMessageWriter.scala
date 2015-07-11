package jac3km4.wakfutcp.Protocol

import java.nio.ByteBuffer

import jac3km4.wakfutcp.Protocol.Output._

trait OutputMessageWriter[T <: OutputMessage] {
  def id: Int

  protected def pack(data: Array[Byte], arch: Byte): ByteBuffer = {
    val len = data.length + 5
    val output = ByteBuffer.allocate(len)
    output.putShort(len.toShort)
    output.put(arch)
    output.putShort(id.toShort)
    output.put(data)
    output
  }

  def write(msg: T): ByteBuffer
}

object OutputMessageWriter {
  // implicits for pimp-my-library pattern

  implicit val authTokenReq = AuthenticationTokenRequestMessage
  implicit val interactiveElementAction = InteractiveElementActionMessage
  implicit val charSelect = CharacterSelectionMessage
  implicit val clientAuthToken = ClientAuthenticationTokenMessage
  implicit val clientDispAuth = ClientDispatchAuthenticationMessage
  implicit val clientProxiesReq = ClientProxiesRequestMessage
  implicit val clientPublicKeyReq = ClientPublicKeyRequestMessage
  implicit val clientVersion = ClientVersionMessage
  implicit val endConnection = EndConnectionMessage
  implicit val partyInfo = PartyInfoRequestMessage
  implicit val giftInventoryReq = GiftInventoryRequestMessage
  implicit val marketConsult = MarketConsultRequestMessage
}