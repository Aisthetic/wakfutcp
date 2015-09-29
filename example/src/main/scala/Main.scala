import akka.actor._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main {
  def main(args: Array[String]) {
    val system = ActorSystem()

    val sniffer = system.actorOf(Props[MarketSniffer])

    Await.result(system.whenTerminated, Duration.Inf)
  }
}
