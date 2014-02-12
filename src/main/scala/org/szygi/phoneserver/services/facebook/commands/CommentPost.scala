package org.szygi.phoneserver.services.facebook.commands

import org.szygi.phoneserver.services.Request
import org.szygi.phoneserver.services.facebook.FacebookServiceContext

case class CommentPost(postRef: Int, comment: String) extends FacebookCommand {
  def execute(ctx: FacebookServiceContext): Unit = {
    val post = ctx.watchedObjects(postRef)

    ctx.api.commentPost(post.id, comment)
  }
}
