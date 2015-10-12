package com.github.wakfutcp.protocol.raw.input

object Implicits {
  implicit val actorMoveTo = ActorMoveToMessage
  implicit val actorSpawn = ActorSpawnMessage
  implicit val authTokenResult = AuthenticationTokenResultMessage
  implicit val characterSelectionResult = CharacterSelectionResultMessage
  implicit val characterList = CharactersListMessage
  implicit val clientAuthResult = ClientAuthenticationResultsMessage
  implicit val clientDispatchAuthResult = ClientDispatchAuthenticationResultMessage
  implicit val clientIp = ClientIpMessage
  implicit val clientProxiesResult = ClientProxiesResultMessage
  implicit val clientPublicKey = ClientPublicKeyMessage
  implicit val clientVersionResult = ClientVersionResultMessage
  implicit val endFight = EndFightMessage
  implicit val fighterMove = FighterMoveMessage
  implicit val fighterPlacementPosition = FightersPlacementPositionMessage
  implicit val fighterTurnEnd = FighterTurnEndMessage
  implicit val forcedDisconnection = ForcedDisconnectionReasonMessage
  implicit val marketConsultResult = MarketConsultResultMessage
  implicit val tableTurnBegin = TableTurnBeginMessage
  implicit val worldSelectionResult = WorldSelectionResultMessage
}
