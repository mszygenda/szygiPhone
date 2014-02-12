package org.szygi.phoneserver.services.facebook

import org.szygi.phoneserver.services._
import org.szygi.phoneserver.services.facebook.commands.{CommandParser}
import org.szygi.phoneserver.services.facebook.api.FacebookApi

object FacebookService extends Service {
  def id = "fb"

  private val ctx = new FacebookServiceContext()

  def createContext = ctx

  def process(req: Request, ctx: ServiceContext) = {
    ctx match {
      case fbCtx: FacebookServiceContext =>
        process(req, fbCtx)
      case _ =>
        throw new IllegalArgumentException("Facebook context expected. Got something different instead")
    }
  }

  private def process(req: Request, ctx: FacebookServiceContext) = {
    val cmd = CommandParser.parse(req)

    cmd.execute(ctx)
  }
}