package org.szygi.phoneserver.services.facebook.commands

import org.szygi.phoneserver.services.facebook.FacebookServiceContext
import org.szygi.phoneserver.services.{Request, Response}

case class ListGroups(req: Request) extends FacebookCommand {
  def execute(ctx: FacebookServiceContext) {
    val response = req.createResponse()
    val groupMenuList = ctx.groups.zipWithIndex.map(groupWithIdx => {
      "G%d. %s" format (groupWithIdx._2, trim(groupWithIdx._1.getName(), 5))
    })

    response.write(groupMenuList.mkString("\n"))

    response.close()
  }

  def trim(str: String, maxLength: Int) = {
    str.take(math.min(str.length, maxLength))
  }
}
