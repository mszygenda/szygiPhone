package org.szygi.phoneserver.sms
import org.szygi.phoneserver.services.{Response, Request}
import akka.actor.ActorRef

class SmsRequest(msg: Message, smsServer: ActorRef) extends Request {
  def number = msg.senderNum

  def serviceId: String = segments(0)

  def action: String = segments(1)

  def contextId: Option[Int] = Some(0)

  def params: List[String] = segments.drop(2)

  def content: String = {
    val contentIdx =  msg.content.indexOf(",") + 1

    msg.content.substring(contentIdx, msg.content.length())
  }

  private val segments: List[String] = {
    println("HEADER" + header)
    header.split('.').toList
  }

  private lazy val header = {
    println("CONTENT")
    println(msg.content)
    msg.content.substring(0, msg.content.indexOf(","))
  }

  def createResponse(): Response = {
    new SmsResponse(this, smsServer)
  }
}
