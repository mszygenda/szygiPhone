package org.szygi.phoneserver.services.facebook.commands

import org.szygi.phoneserver.services.Request
import org.szygi.phoneserver.services.facebook.FacebookServiceContext
import org.szygi.phoneserver.services.facebook.api.ObjectType

case class Like(objectRef: Int, req: Request) extends FacebookCommand {
  def execute(ctx: FacebookServiceContext): Unit = {
    val fbObject = ctx.watchedObjects(objectRef)

    ctx.api.like(fbObject.id, fbObject.objectType)
  }
}
