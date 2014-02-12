package org.szygi.phoneserver.services.facebook.commands

import org.szygi.phoneserver.services.Request
import org.szygi.phoneserver.services.facebook.{FacebookObject, FacebookServiceContext}
import facebook4j.{ Comment => Comment4j }
import org.szygi.phoneserver.services.facebook.api.Comment

case class GetPostComments(req: Request, postRef: Int, offset: Int, amount: Int) extends FacebookCommand {
  def execute(ctx: FacebookServiceContext): Unit = {
    val post = ctx.watchedObjects(postRef)
    val comments = ctx.api.getPostComments(post.id, offset, amount)
    val response = req.createResponse()

    response.write(comments.map(handleComment(ctx, _)).mkString("\n"))
    response.close()
  }

  def handleComment(ctx: FacebookServiceContext, comment: Comment4j) = {
    val commentObj = new FacebookObject(comment.getId(), Comment)
    val commentRef = ctx.registerObject(commentObj)

    "C%s. %s: %s" format(commentRef, comment.getFrom().getName(), comment.getMessage())
  }
}
