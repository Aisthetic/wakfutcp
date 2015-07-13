package com.github.jac3km4.wakfutcp

import java.net.InetSocketAddress
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

import akka.actor._
import akka.io.Tcp.Connected
import com.github.jac3km4.wakfutcp.Protocol.Domain.Version
import com.github.jac3km4.wakfutcp.Protocol.Input._
import com.github.jac3km4.wakfutcp.Protocol.Output._
import com.github.jac3km4.wakfutcp.WorldDispatcher.{Credentials, WorldAuthToken}

object WorldDispatcher {

  def props(client: ActorRef, credentials: Credentials) =
    Props(classOf[WorldDispatcher], client, credentials)

  case class Credentials(login: String, password: String)

  case class WorldAuthToken(token: String)

}

class WorldDispatcher(val client: ActorRef, val credentials: Credentials)
  extends Actor with ActorLogging with Stash with Messenger {

  import context._

  val OriginalVersion = Version(1, 43, 10)

  def receive = {
    case Connected(l, r) =>
      become(obtainRequiredVersion(sender()))
  }

  /*
    fetch the required version,
    to pretend that we have it
  */
  def obtainRequiredVersion(connection: ActorRef): Receive = {
    case ClientIpMessage(_) =>
      connection ! wrap(ClientVersionMessage(OriginalVersion))

    case ClientVersionResultMessage(success, required) =>
      log.info("server version: {}", required)
      connection ! wrap(ClientPublicKeyRequestMessage(8))
      become(connectToWorld(connection, required))
  }

  def connectToWorld(connection: ActorRef, version: Version): Receive = {
    case ClientPublicKeyMessage(salt, publicKey) =>
      val cert = new X509EncodedKeySpec(publicKey)
      val keyFactory = KeyFactory.getInstance("RSA")
      val cipher = Cipher.getInstance("RSA")
      cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(cert))
      connection ! wrap(ClientDispatchAuthenticationMessage
        .create(credentials.login, credentials.password, salt, cipher))

    case ClientDispatchAuthenticationResultMessage(result, _, _) =>
      import ClientDispatchAuthenticationResultMessage._
      result match {
        case Success() =>
          log.info("succesfully logged into the server")
          connection ! wrap(ClientProxiesRequestMessage())
        case message =>
          throw AuthenticationException(s"Login failed with $message")
      }

    case ClientProxiesResultMessage(proxies, worlds) =>
      client ! ServerList(proxies, worlds)

    case ServerChoice(server) =>
      connection ! wrap(AuthenticationTokenRequestMessage(server.id, 0))
      val accessor = actorOf(Props(classOf[WorldAccessor], client, version))
      actorOf(Props(classOf[WakfuTcpClient], accessor,
        new InetSocketAddress(server.address, server.ports(0))))
      become(awaitAuthToken(accessor))
  }

  def awaitAuthToken(world: ActorRef): Receive = {
    case AuthenticationTokenResultMessage.Success(token) =>
      world ! WorldAuthToken(token)
      sender() ! wrap(EndConnectionMessage())

    case AuthenticationTokenResultMessage.Failure() =>
      throw AuthenticationException("Failed to receive authentication token")
  }
}
