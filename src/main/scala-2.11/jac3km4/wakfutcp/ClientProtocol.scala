package jac3km4.wakfutcp

import jac3km4.wakfutcp.Protocol.Domain.{Character, Proxy, WorldInfo}

case class ServerList(proxies: Array[Proxy], worldInfos: Array[WorldInfo])

case class CharacterList(characters: Array[Character])

case class ServerChoice(proxy: Proxy)

case class CharacterChoice(character: Character)

case class ConnectedToWorld()
