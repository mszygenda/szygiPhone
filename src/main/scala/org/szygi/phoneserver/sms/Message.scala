package org.szygi.phoneserver.sms

class Message(val senderNum: String, val receiverNum: String, val content: String) {

}

object Message {
  val smsPartsSeparator = "\n##\n"

  def apply(raw: String) = {
    val msgParts = raw.split(smsPartsSeparator)

    new Message(msgParts(0), msgParts(1), msgParts(2))
  }
}
