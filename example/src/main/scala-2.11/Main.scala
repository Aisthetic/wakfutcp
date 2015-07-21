import java.net.InetSocketAddress

import akka.actor._
import com.github.wakfutcp.WorldDispatcher.Credentials
import com.github.wakfutcp.{WakfuTcpClient, WorldDispatcher}
import com.typesafe.config.ConfigFactory

object Main {
  def main(args: Array[String]) {
    val factory = ConfigFactory.load()
    val account = factory.getConfig("account")
    val gate = factory.getConfig("gate")
    val system = ActorSystem()

    val mallrat = system.actorOf(Props[MarketSniffer])

    // this handles connection
    val dispatcher = system.actorOf(Props(classOf[WorldDispatcher], mallrat,
      Credentials(account.getString("login"), account.getString("password"))))
    val tcpClient = system.actorOf(Props(classOf[WakfuTcpClient], dispatcher,
      new InetSocketAddress(gate.getString("address"), gate.getInt("port"))))
  }
}
