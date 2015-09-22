# Synopsis
This is a TCP client which can connect to Wakfu game servers and interact with them.
By default it contains a set of messages and routines to perform authentication and some other basic tasks.
It uses the actor pattern and integrates with the Akka framework.

# Code example
Market reading actor:
```scala
def receive = {
    case ServerList(servers, _) =>
      sender() ! ServerChoice(servers.find(_.name == "Nox").get)
    case CharacterList(characters) =>
      sender() ! CharacterChoice(characters.find(_.name == "Derp").get)
    case ConnectedToWorld() =>
      sender() ! wrap(InteractiveElementActionMessage(12224, 12))
    case MarketConsultResultMessage(sales, count) =>
      sender() ! wrap(MarketConsultRequestMessage(Array.empty, -1, -1, -1, -1, 10.toShort, lowestMode = false))
      become(receiveAndAsk(sales.toList, 10))
  }

  def receiveAndAsk(entries: List[MarketEntry], start: Int): Receive = {
    case MarketConsultResultMessage(sales, count) =>
      val next = start + 10
      if (next >= count)
        handleData(entries ::: sales.toList)
      else {
        sender() ! wrap(MarketConsultRequestMessage(Array.empty, -1, -1, -1, -1, next.toShort, lowestMode = false))
        become(receiveAndAsk(entries ::: sales.toList, next))
      }
  }

  def handleData(data: List[MarketEntry]) = {
    // stuff
    stop(self)
  }
```

# Running
To use it you need sbt and scala compiler.

The easiest way to check it out is to clone this repository and run `sbt run` in the `example` directory