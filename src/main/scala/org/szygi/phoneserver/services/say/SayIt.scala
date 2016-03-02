package org.szygi.phoneserver.services.say

import org.szygi.phoneserver.services.{Request, ServiceContext, Command}
import sys.process._

case class SayIt(req: Request, phoneNumber: String, delay: Int, times: String, message: String, voice: String) extends Command {
  private val sayToCmd = "tools/sayTo"

  def execute(ctx: ServiceContext): Unit = {
    println("Saying phrase '%s' to number '%s'" format (message, phoneNumber))

    val cmd = Process(Seq("bash", sayToCmd, phoneNumber, delay.toString(), times.toString(), message, voice))
    cmd.run()
  }
}
