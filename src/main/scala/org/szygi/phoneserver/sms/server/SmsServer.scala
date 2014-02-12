package org.szygi.phoneserver.sms.server

import akka.actor.{Props, Actor, ActorRef}
import org.szygi.phoneserver.sms.Message
import sys.process._
import scala.concurrent.duration._

class SmsServer extends Actor {
  private val sendCmd = "/home/szygi/Praca/becauseican/phone-server/tools/sendSms"
  private val fetchCmd: String = "/home/szygi/Praca/becauseican/phone-server/tools/fetchSms.sh"

  private var messageListeners: List[ActorRef] = List()

  def receive = {
    case RegisterListener(listener: ActorRef) => {
      messageListeners = listener :: messageListeners
    }
    case SendSms(msg: Message) => {
      sendSms(msg)
    }
    case CheckForMessages => {
      val inbox = fetchMessages()

      inbox.foreach(msg => {
        messageListeners.foreach(_ ! msg)
      })
    }
    case CheckForMessagesPeriodically => {
      val system = context.system
      import system.dispatcher

      system.scheduler.schedule(500 milliseconds, 3000 milliseconds, self, CheckForMessages)
    }
  }

  private def fetchMessages(): Seq[Message] = {
    val out = (Seq("bash", fetchCmd, "2>", "/dev/null") !!).trim()
    val msgSeparator = "\n####\n"

    if (out.length() == 0) {
      return Seq()
    }

    val rawMessages = out.split(msgSeparator)
    rawMessages.map(parseRawMessage).filter(_ != null)
  }

  /**
   * Parses raw message or returns null if it's not valid
   * @param rawMsg
   * @return
   */
  private def parseRawMessage(rawMsg: String) = {
    try {
      Message(rawMsg)
    } catch {
      case e: Exception => {
        println("Failed to parse raw message. Content <%s>" format rawMsg)

        null
      }
    }
  }

  private val SmsCharactersLimit = 160

  private def sendSms(msg: Message) {
    if (msg.content.length > SmsCharactersLimit) {
      sendSms(new Message(msg.senderNum, msg.receiverNum, msg.content.substring(0, SmsCharactersLimit)))
      sendSms(new Message(msg.senderNum, msg.receiverNum, msg.content.substring(SmsCharactersLimit - 1, msg.content.length())))

      return
    }

    println("Trying to send sms to %s content: %s" format (msg.receiverNum, msg.content))

    Seq("bash", sendCmd, msg.receiverNum, msg.content) ! match {
      case 0 =>
        println("Message successfully sent to number %s" format msg.receiverNum)
      case _: Int => { 
        println("Failed to send sms to number %s. Retrying" format msg.receiverNum)
        self ! SendSms(msg)
      }
    }
  }
}
