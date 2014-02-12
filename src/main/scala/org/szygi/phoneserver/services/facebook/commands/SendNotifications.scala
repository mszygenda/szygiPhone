package org.szygi.phoneserver.services.facebook.commands

import org.szygi.phoneserver.services.facebook.FacebookServiceContext
import org.szygi.phoneserver.services.Request

class SendNotifications extends FacebookCommand {
  def execute(ctx: FacebookServiceContext): Unit = {
    ctx.sendNotifications = true
  }
}
