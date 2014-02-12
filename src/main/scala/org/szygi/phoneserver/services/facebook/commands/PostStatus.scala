package org.szygi.phoneserver.services.facebook.commands

import org.szygi.phoneserver.services.facebook.api._
import org.szygi.phoneserver.services.facebook.{FacebookObject, FacebookServiceContext}
import org.szygi.phoneserver.services.Request

case class PostStatus(req: Request, content: String) extends FacebookCommand {
  def execute(ctx: FacebookServiceContext) {
    val postId = ctx.api.postStatus(content)
    val resp = req.createResponse()
    val postRef = ctx.registerObject(FacebookObject(postId, Post))

    resp.close("Created P%s. %s" format (postRef, content))
  }
}
