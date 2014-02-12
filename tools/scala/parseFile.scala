import java.io.FileInputStream
import scala.io._

import scala.util.matching._

object ParseFile {
  def main(args: Array[String]) {
    try {
      val path = args(0)
      val output = Source.fromFile(path, "utf-8").mkString
      val messages = output.split("Slot:")

      val phoneServerInput = messages.map(formatMessage(_)).filter(_.length() > 0).mkString("\n####\n")
      println(phoneServerInput)
    } catch {
      case e: Exception => 
    }
  }

  def formatMessage(msg: String): String = {
    val senderNumRegex = """From: (\+?\d+)""".r
    val receiverNumRegex = """SMSC number: (\+?\d+)""".r

    (senderNumRegex.findFirstMatchIn(msg), receiverNumRegex.findFirstMatchIn(msg)) match {
      case (Some(fromMatch), Some(receiverMatch)) => {
        val fromNum = fromMatch.group(1)
        val receiverNum = receiverMatch.group(1)
        val content = msg.split("\n\n")(1)

        return joinSegments(Seq(fromNum, receiverNum, content))
      }
      case (_,_) => ""
    }
  }

  def joinSegments(segments: Seq[String]) = {
    segments.mkString("\n##\n")
  }

  def createSegment(data: String) = {
    "%s\n##\n" format data
  }
}

// vim: set ts=4 sw=4 et:
