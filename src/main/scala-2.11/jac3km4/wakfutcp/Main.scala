package jac3km4.wakfutcp

import java.net.InetSocketAddress

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import jac3km4.wakfutcp.WorldDispatcher.Credentials

object Main {
  def main(args: Array[String]) {
    val factory = ConfigFactory.load()
    val account = factory.getConfig("account")
    val gate = factory.getConfig("gate")
    val system = ActorSystem()

    // this is the front-end client
    val mallrat = system.actorOf(Props[Mallrat])

    // this handles connection
    val dispatcher = system.actorOf(Props(classOf[WorldDispatcher], mallrat,
      Credentials(account.getString("login"), account.getString("password"))))
    val client = system.actorOf(Props(classOf[WakfuTcpClient], dispatcher,
      new InetSocketAddress(gate.getString("address"), gate.getInt("port"))))
  }
}
