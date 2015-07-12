package jac3km4.wakfutcp

import java.net.InetSocketAddress
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

import akka.actor._
import akka.io.Tcp.Connected
import jac3km4.wakfutcp.Protocol.Domain.Version
import jac3km4.wakfutcp.Protocol.Input._
import jac3km4.wakfutcp.Protocol.Output._
import jac3km4.wakfutcp.WakfuTcpClient.Inbound
import jac3km4.wakfutcp.WorldDispatcher.{Credentials, WorldAuthToken}

object WorldDispatcher {

  def props(client: ActorRef, credentials: Credentials) =
    Props(classOf[WorldDispatcher], client, credentials)

  case class Credentials(login: String, password: String)

  case class WorldAuthToken(token: String)

}

class WorldDispatcher(client: ActorRef, credentials: Credentials)
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
    case Inbound(buf) =>
      val id = buf.getShort
      id match {
        case 110 =>
          connection ! wrap(ClientVersionMessage(OriginalVersion))
        case 8 =>
          val versionMessageResult = ClientVersionResultMessage.read(buf)
          log.info("server version: {}", versionMessageResult.required)
          connection ! wrap(ClientPublicKeyRequestMessage(8))
          become(connectToWorld(connection, versionMessageResult.required))
      }
  }

  def connectToWorld(connection: ActorRef, version: Version): Receive = {
    case Inbound(buf) =>
      val id = buf.getShort
      id match {
        case 1034 =>
          val publicKeyMessage = ClientPublicKeyMessage.read(buf)
          val cert = new X509EncodedKeySpec(publicKeyMessage.publicKey)
          val keyFactory = KeyFactory.getInstance("RSA")
          val cipher = Cipher.getInstance("RSA")
          cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(cert))
          connection ! wrap(
            ClientDispatchAuthenticationMessage
              .create(credentials.login, credentials.password, publicKeyMessage.salt, cipher)
          )
        case 1027 =>
          import ClientDispatchAuthenticationResultMessage._
          val authenticationResultMessage = ClientDispatchAuthenticationResultMessage.read(buf)
          authenticationResultMessage.result match {
            case Success() =>
              log.info("succesfully logged into the server")
              connection ! wrap(ClientProxiesRequestMessage())
            case message =>
              log.error("failed during login with {}", message)
              stop(self)
          }
        case 1036 =>
          val proxiesResultMessage = ClientProxiesResultMessage.read(buf)
          client ! ServerList(proxiesResultMessage.proxies, proxiesResultMessage.worlds)
        case _ =>
      }
    case ServerChoice(server) =>
      connection ! wrap(AuthenticationTokenRequestMessage(server.id, 0))
      val accessor = actorOf(Props(classOf[WorldAccessor], client, version))
      actorOf(Props(classOf[WakfuTcpClient], accessor,
        new InetSocketAddress(server.address, server.ports(0))))
      become(awaitAuthToken(accessor))
  }

  def awaitAuthToken(world: ActorRef): Receive = {
    case Inbound(buf) =>
      val id = buf.getShort
      id match {
        case 1212 =>
          import AuthenticationTokenResultMessage._
          AuthenticationTokenResultMessage.read(buf) match {
            case Success(token) =>
              world ! WorldAuthToken(token)
              sender() ! wrap(EndConnectionMessage())
              unstashAll()
              watch(world)
              become(watchAccessor(world))
            case Failure() =>
              log.error("failed to receive authentication token")
              stop(self)
          }
        case _ =>
      }
    case _ =>
      stash()
  }

  def watchAccessor(world: ActorRef): Receive = {
    case _: Terminated =>
      log.info("shutting down")
      system.shutdown()
  }
}
