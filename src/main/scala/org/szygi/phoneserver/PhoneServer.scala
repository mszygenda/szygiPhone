package org.szygi.phoneserver

import akka.actor.{Props, ActorSystem}
import org.szygi.phoneserver.services.ServerActor
import org.szygi.phoneserver.sms.server.{CheckForMessagesPeriodically, SmsServer}
import org.szygi.phoneserver.sms.RequestPasser

object PhoneServer {
  val system = ActorSystem("phone-server")
  val serverAgent = system.actorOf(Props[ServerActor], "Server")
  val smsServer = system.actorOf(Props[SmsServer], "SmsServer")
  val requestPasser = system.actorOf(Props(new RequestPasser(serverAgent, smsServer)))

  def main(args: Array[String]) {
    smsServer ! CheckForMessagesPeriodically
  }
}
