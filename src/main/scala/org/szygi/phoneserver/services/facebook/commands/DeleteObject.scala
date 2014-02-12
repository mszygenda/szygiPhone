package org.szygi.phoneserver.services.facebook.commands

import org.szygi.phoneserver.services.facebook.FacebookServiceContext

case class DeleteObject(objectRef: Int) extends FacebookCommand {
  def execute(ctx: FacebookServiceContext) {
    val obj = ctx.watchedObjects(objectRef)

    ctx.api.removeObject(obj.id, obj.objectType)
  }
}
