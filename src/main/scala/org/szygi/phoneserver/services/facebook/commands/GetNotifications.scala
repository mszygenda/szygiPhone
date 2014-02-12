package org.szygi.phoneserver.services.facebook.commands

import org.szygi.phoneserver.services.facebook.{PeriodicCommandInvoker, FacebookServiceContext}
import org.szygi.phoneserver.services.Request
import facebook4j.Notification

case class GetNotifications(req: Request, startWatching: Boolean) extends FacebookCommand {
  def execute(ctx: FacebookServiceContext): Unit = {
    ctx.api.getNotificationsAndMarkAsReaded() match {
      case List() => println("No new notifications")
      case notifications: List[Notification] => {
        val reply = notifications.map(_.getTitle).mkString(";\n")
        val response = req.createResponse()

        response.write(reply)
        response.close()
      }
    }

    if (startWatching) {
      enableWatcher(ctx, new GetNotifications(req, false))
    }
  }

  def enableWatcher(ctx: FacebookServiceContext, cmd: GetNotifications) {
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
