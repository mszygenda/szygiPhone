package org.szygi.phoneserver.services.facebook.commands

import org.szygi.phoneserver.services.facebook.FacebookServiceContext

case object DisableNotifications extends FacebookCommand {
  def execute(ctx: FacebookServiceContext): Unit = {
    ctx.notificationWatcher match {
      case Some(watcher) => {
        println("Notifications disabled")

        watcher.disable
      }
      case None => {
        println("Notification watcher wasn't turned on")
      }
    }
  }
}
