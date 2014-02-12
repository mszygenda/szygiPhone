package org.szygi.phoneserver.sms
import org.szygi.phoneserver.services.Response
import akka.actor.ActorRef
import org.szygi.phoneserver.sms.server.{SendSms, SmsServer}

class SmsResponse(req: SmsRequest, smsServer: ActorRef) extends Response {
  var chunks: List[String] = List()

  def abandon(): Unit = {
    println("Ababandoing response")
  }

  def write(data: String): Unit = {
    chunks = data :: chunks
  }

  def close(): Unit = {
    val sms = new Message(null, req.number, chunks.mkString(""))

    println("Response closed")
    println("Sending response '%s' to '%s'" format (chunks.mkString(""), req.number))

    smsServer ! SendSms(sms)
  }
}
