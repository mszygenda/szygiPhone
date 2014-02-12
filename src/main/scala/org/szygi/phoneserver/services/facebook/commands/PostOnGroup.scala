package org.szygi.phoneserver.services.facebook.commands

import org.szygi.phoneserver.services.facebook.{FacebookObject, FacebookServiceContext}
import org.szygi.phoneserver.services.Request
import org.szygi.phoneserver.services.facebook.api.Post

case class PostOnGroup(req: Request, id: Int, content: String) extends FacebookCommand {
  def execute(ctx: FacebookServiceContext) {
    val group = ctx.groups(id)
    val postId = ctx.api.postOnGroup(group.getId(), content)
    val postRef = ctx.registerObject(FacebookObject(postId, Post))
    val resp = req.createResponse()

    resp.close("Created P%s. %s" format (postRef, content))
  }
}
