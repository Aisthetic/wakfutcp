package com.github.wakfutcp.protocol.client.input

import com.github.wakfutcp.protocol.domain.{WorldInfo, Proxy}

final case class ServerList(proxies: Array[Proxy], worldInfos: Array[WorldInfo])