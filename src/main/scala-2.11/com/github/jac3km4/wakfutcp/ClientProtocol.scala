package com.github.jac3km4.wakfutcp

import com.github.jac3km4.wakfutcp.Protocol.Domain
import com.github.jac3km4.wakfutcp.Protocol.Domain.WorldInfo

case class ServerList(proxies: Array[Domain.Proxy], worldInfos: Array[WorldInfo])

case class CharacterList(characters: Array[Domain.Character])

case class ServerChoice(proxy: Domain.Proxy)

case class CharacterChoice(character: Domain.Character)

case class ConnectedToWorld()

case class Disconnect()