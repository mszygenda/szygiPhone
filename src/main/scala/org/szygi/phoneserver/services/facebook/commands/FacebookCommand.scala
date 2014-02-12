package org.szygi.phoneserver.services.facebook.commands

import org.szygi.phoneserver.services.facebook.FacebookServiceContext
import org.szygi.phoneserver.services.Response

trait FacebookCommand {
  def execute(ctx: FacebookServiceContext)
}



