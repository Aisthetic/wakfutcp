package com.github.wakfutcp

import java.net.InetSocketAddress
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

import akka.actor._
import com.github.wakfutcp.Exceptions.AuthenticationException
import com.github.wakfutcp.WakfuConnector._
import com.github.wakfutcp.WakfuTcpClient.Disconnect
import com.github.wakfutcp.protocol.client.input.ServerList
import com.github.wakfutcp.protocol.client.output.{LogOut, ServerChoice}
import com.github.wakfutcp.protocol.domain.Version
import com.github.wakfutcp.protocol.raw.input._
import com.github.wakfutcp.protocol.raw.output.Implicits._
import com.github.wakfutcp.protocol.raw.output._
import com.typesafe.config.{Config, ConfigFactory}

object WakfuConnector {

  def props(client: ActorRef, credentials: Credentials, config: Option[Config]) =
    Props(classOf[WakfuConnector], client, credentials, config)

  sealed trait State

  sealed trait Data

  case object Uninitialized extends Data

  case class RequiredVersion(version: Version) extends Data

  case class TokenRecipient(recipient: ActorRef) extends Data

  case object VerifyVersion extends State

  case object ConnectToWorld extends State

  case object ForwardToken extends State

  // messages
  case class WorldAuthToken(token: String)

  case class Credentials(login: String, password: String)

}

class WakfuConnector(val client: ActorRef,
                     credentials: Credentials,
                     confOpt: Option[Config])
  extends FSM[State, Data] with ActorLogging with Messenger {

  import context._

  val config: Config = confOpt getOrElse ConfigFactory.load().getConfig("wakfutcp.default")

  val connection: ActorRef = actorOf(
    Props(
      classOf[WakfuTcpClient],
      self,
      new InetSocketAddress(config.getString("gate.address"), config.getInt("gate.port"))
    )
  )

  startWith(VerifyVersion, Uninitialized)

  when(VerifyVersion) {
    case Event(ClientIpMessage(_), _) ⇒
      val v = config.getString("gameVersion").split('.')
      sender() ! wrap(ClientVersionMessage(Version(v(0).toInt, v(1).toInt, v(2).toInt)))
      stay()

    case Event(ClientVersionResultMessage(success, required), _) ⇒
      log.info("server version: {}", required)
      connection ! wrap(ClientPublicKeyRequestMessage(8))
      goto(ConnectToWorld) using RequiredVersion(required)
  }

  when(ConnectToWorld) {
    case Event(ClientPublicKeyMessage(salt, pubKey), _) ⇒
      val cert = new X509EncodedKeySpec(pubKey)
      val keyFactory = KeyFactory.getInstance("RSA")
      val cipher = Cipher.getInstance("RSA")
      cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(cert))
      connection ! wrap(
        ClientDispatchAuthenticationMessage
          .create(
            credentials.login,
            credentials.password,
            salt,
            cipher
          )
      )
      stay()

    case Event(ClientDispatchAuthenticationResultMessage(result, _, _), _) ⇒
      import ClientDispatchAuthenticationResultMessage.Success
      result match {
        case Success ⇒
          log.info("succesfully logged into the server")
          sender() ! wrap(ClientProxiesRequestMessage())
        case message ⇒
          throw AuthenticationException(s"Login failed with $message")
      }
      stay()

    case Event(ClientProxiesResultMessage(proxies, worlds), _) ⇒
      client ! ServerList(proxies, worlds)
      stay()

    case Event(LogOut, _) ⇒
      log.info("logging out")
      connection ! wrap(EndConnectionMessage())
      connection ! Disconnect
      system.terminate()
      stop()

    case Event(ServerChoice(server), RequiredVersion(version)) ⇒
      connection ! wrap(AuthenticationTokenRequestMessage(server.id, 0))
      val accessor = actorOf(Props(
        classOf[WakfuWorldAccessor],
        client,
        new InetSocketAddress(server.address, server.ports(0)),
        if (config.getBoolean("useServerVersion")) version
        else {
          val v = config.getString("gameVersion").split('.')
          Version(v(0).toInt, v(1).toInt, v(2).toInt)
        }
      ))

      watch(accessor)

      goto(ForwardToken) using TokenRecipient(accessor)
  }

  when(ForwardToken) {
    case Event(AuthenticationTokenResultMessage.Success(token), TokenRecipient(recipient)) ⇒
      recipient ! WorldAuthToken(token)
      sender() ! wrap(EndConnectionMessage())
      stay()

    case Event(AuthenticationTokenResultMessage.Failure, _) ⇒
      throw AuthenticationException("Failed to receive authentication token")

    case Event(LogOut, _) ⇒
      connection ! Disconnect
      system.terminate()
      stop()
  }
}
