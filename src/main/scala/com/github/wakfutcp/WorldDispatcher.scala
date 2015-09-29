package com.github.wakfutcp

import java.net.InetSocketAddress
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

import akka.actor._
import com.github.wakfutcp.Exceptions._
import com.github.wakfutcp.WorldDispatcher._
import com.github.wakfutcp.protocol.client.input._
import com.github.wakfutcp.protocol.client.output._
import com.github.wakfutcp.protocol.domain.Version
import com.github.wakfutcp.protocol.raw.input._
import com.github.wakfutcp.protocol.raw.output._

object WorldDispatcher {

  def props(client: ActorRef) =
    Props(classOf[WorldDispatcher], client)

  // states
  case object Uninitialized extends Data

  case class Credentials(login: String, password: String) extends Data

  case class ConnectionData(connection: ActorRef, credentials: Credentials, version: Version) extends Data

  case class TokenRecipient(recipient: ActorRef) extends Data

  case object AwaitLogin extends State

  case object VerifyVersion extends State

  case object ConnectToWorld extends State

  case object ForwardAuthToken extends State

  // messages
  case class WorldAuthToken(token: String)

}

class WorldDispatcher(val client: ActorRef)
  extends FSM[State, Data] with ActorLogging with Messenger {

  import context._

  val OriginalVersion = Version(1, 43, 10)

  startWith(AwaitLogin, Uninitialized)

  when(AwaitLogin) {
    case Event(LogIn(l, p), Uninitialized) ⇒
      goto(VerifyVersion) using Credentials(l, p)
  }

  when(VerifyVersion) {
    case Event(ClientIpMessage(_), credentials) ⇒
      sender() ! wrap(ClientVersionMessage(OriginalVersion))
      stay()

    case Event(ClientVersionResultMessage(success, required), credentials: Credentials) ⇒
      log.info("server version: {}", required)
      val con = sender()
      con ! wrap(ClientPublicKeyRequestMessage(8))
      goto(ConnectToWorld) using ConnectionData(con, credentials, required)
  }

  when(ConnectToWorld) {
    case Event(ClientPublicKeyMessage(salt, pubKey), ConnectionData(_, credentials, _)) ⇒
      val cert = new X509EncodedKeySpec(pubKey)
      val keyFactory = KeyFactory.getInstance("RSA")
      val cipher = Cipher.getInstance("RSA")
      cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(cert))
      sender() ! wrap(ClientDispatchAuthenticationMessage
        .create(credentials.login, credentials.password, salt, cipher))
      stay()

    case Event(ClientDispatchAuthenticationResultMessage(result, _, _), _) ⇒
      import ClientDispatchAuthenticationResultMessage._
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

    case Event(ServerChoice(server), ConnectionData(con, _, version)) ⇒
      con ! wrap(AuthenticationTokenRequestMessage(server.id, 0))
      val accessor = actorOf(Props(classOf[WorldAccessor], client, version))
      actorOf(Props(classOf[WakfuTcpClient], accessor,
        new InetSocketAddress(server.address, server.ports(0))))
      goto(ForwardAuthToken) using TokenRecipient(accessor)
  }

  when(ForwardAuthToken) {
    case Event(AuthenticationTokenResultMessage.Success(token), TokenRecipient(recipient)) ⇒
      recipient ! WorldAuthToken(token)
      sender() ! wrap(EndConnectionMessage())
      stay()

    case Event(AuthenticationTokenResultMessage.Failure, _) ⇒
      throw AuthenticationException("Failed to receive authentication token")
  }
}
