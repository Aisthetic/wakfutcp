import java.net.InetSocketAddress

import akka.actor._
import com.github.wakfutcp.protocol.client.output.LogIn
import com.github.wakfutcp.{WakfuTcpClient, WorldDispatcher}
import com.typesafe.config.ConfigFactory

object Main {
  def main(args: Array[String]) {
    val factory = ConfigFactory.load()
    val account = factory.getConfig("account")
    val gate = factory.getConfig("gate")
    val system = ActorSystem()

    val sniffer = system.actorOf(Props[MarketSniffer])

    // this handles connection
    val dispatcher = system.actorOf(Props(classOf[WorldDispatcher], sniffer))
    val tcpClient = system.actorOf(Props(classOf[WakfuTcpClient], dispatcher,
      new InetSocketAddress(gate.getString("address"), gate.getInt("port"))))

    dispatcher ! LogIn(account.getString("login"), account.getString("password"))
  }
}
