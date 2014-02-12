package org.szygi.phoneserver.services.facebook.commands

import org.szygi.phoneserver.services.facebook.PeriodicCommandInvoker
import org.szygi.phoneserver.services.facebook.FacebookServiceContext
import org.szygi.phoneserver.services.facebook.{PeriodicCommandInvoker, FacebookServiceContext}
import org.szygi.phoneserver.services.Request
import facebook4j.{Message, Notification}
import org.szygi.phoneserver.services.Request
import org.szygi.phoneserver.services.Request
import org.szygi.phoneserver.services.facebook.FacebookServiceContext

case class GetInbox(req: Request, startWatching: Boolean) extends FacebookCommand {
  def execute(ctx: FacebookServiceContext): Unit = {
    ctx.api.getNewMessagesAndMarkAsReaded() match {
      case List() => println("No new messages")
      case messages: List[Message] => {
        val reply = messages.map(handleMessage).mkString(";\n")
        val response = req.createResponse()

        response.write(reply)
        response.close()
      }
    }

    if (startWatching) {
      enableWatcher(ctx, new GetInbox(req, false))
    }
  }

  def handleMessage(msg: Message) = {
    "%s %d:%d : %s" format (msg.getFrom().getName, msg.getCreatedTime().getHours(), msg.getCreatedTime().getMinutes(), msg.getMessage())
  }

  def enableWatcher(ctx: FacebookServiceContext, cmd: FacebookCommand) {
    ctx.notificationWatcher match {
      case None => {
        val watcher = new PeriodicCommandInvoker(cmd, ctx)

        watcher.enable

        ctx.notificationWatcher = Some(watcher)
      }
      case Some(watcher) => {
        watcher.enable
      }
    }
  }
}

