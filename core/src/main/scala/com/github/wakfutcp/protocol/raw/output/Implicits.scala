package com.github.wakfutcp.protocol.raw.output

object Implicits {
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
