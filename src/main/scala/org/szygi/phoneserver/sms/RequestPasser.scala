package org.szygi.phoneserver.sms

import akka.actor.{Actor, ActorRef}
import org.szygi.phoneserver.sms.server.RegisterListener

class RequestPasser(serverAgent: ActorRef, smsServer: ActorRef) extends Actor {
  smsServer ! RegisterListener(self)

  def receive = {
    case msg: Message => {
      serverAgent ! new SmsRequest(msg, smsServer)
    }
  }
}
