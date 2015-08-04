package com.github.wakfutcp.Protocol

import java.nio.ByteBuffer

import com.github.wakfutcp.Protocol.Output._

trait OutputMessageWriter[T <: OutputMessage] {
  def id: Int

  protected def pack(data: Array[Byte], arch: Byte): ByteBuffer = {
    val len = data.length + 5
    ByteBuffer.allocate(len)
      .putShort(len.toShort)
      .put(arch)
      .putShort(id.toShort)
      .put(data)
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
  implicit val fightCreation = FightCreationRequestMessage
  implicit val fighterEndTurnReq = FighterEndTurnRequestMessage
  implicit val fighterReadyReq = FighterReadyRequestMessage
  implicit val fighterTurnEndAck = FighterTurnEndAckMessage
  implicit val spellCastReq = SpellLevelCastRequestMessage
}