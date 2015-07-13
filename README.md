# wakfutcp
This is a TCP client written in Scala, using the
Akka framework. It's goal is to perform interactions
with the wakfu game servers like a regular game client.

The idea is basically that you create a client actor which makes use
of the connection Actors to interact with the game.

Here's an example market reading Actor:

```scala
def receive = {
    case ServerList(servers, _) =>
      sender() ! ServerChoice(servers.find(_.name == "Nox").get)
    case CharacterList(characters) =>
      sender() ! CharacterChoice(characters.find(_.name == "Derp").get)
    case ConnectedToWorld() =>
      /*
        command used to check the market
        on the astrub board
       */
      sender() ! wrap(InteractiveElementActionMessage(18744, 12))
    case MarketConsultResultMessage(sales, count) =>
      /*
        market items come by 10
       */
      for (i <- 10 until count by 10)
        sender() ! wrap(MarketConsultRequestMessage(Array.empty, -1, -1, -1, -1, i.toShort, lowestMode = true))
      unstashAll()
      become(receiveSales(sender()))
    case _ => stash()
  }

  def receiveSales(connection: ActorRef): Receive = {
    case MarketConsultResultMessage(sales, count) =>
    // do something with them...
  }
```
