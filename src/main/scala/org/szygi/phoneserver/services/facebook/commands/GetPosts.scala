package org.szygi.phoneserver.services.facebook.commands

import org.szygi.phoneserver.services.facebook.{FacebookObject, FacebookServiceContext}
import org.szygi.phoneserver.services.Request
import facebook4j.{Post => Post4j }
import org.szygi.phoneserver.services.facebook.api._

case class GetPosts(req: Request, groupRef: Option[Int], offset: Int, amount: Int) extends FacebookCommand {
  def execute(ctx: FacebookServiceContext): Unit = {
    val posts = groupRef match {
      case Some(groupRef) => {
        val group = ctx.groups(groupRef)
        ctx.api.getGroupPosts(group.getId(), offset, amount)
      }
      case None => {
        ctx.api.getWall(offset, amount)
      }
    }
    val response = req.createResponse()

    response.write(posts.map(handlePost(ctx, _)).mkString("\n"))
    response.close()
  }

  private def handlePost(ctx: FacebookServiceContext, post: Post4j) = {
    val refId = ctx.registerObject(FacebookObject(post.getId(), Post))
    val content = if(post.getMessage() != null) post.getMessage() else post.getStory()

    "P%s. %s: %s" format (refId, post.getFrom().getName(), content)
  }
}
