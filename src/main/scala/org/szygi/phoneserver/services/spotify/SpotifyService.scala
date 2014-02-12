package org.szygi.phoneserver.services.spotify
import org.szygi.phoneserver.services._
import org.szygi.phoneserver.NoContext

class SpotifyService extends Service {
  def id: String = "spotify"

  def createContext: ServiceContext = NoContext

  def process(req: Request, context: ServiceContext): Unit = {
    val cmd = req.action match {
      case "play" => {
        new PlaySongCommand(req.number, req.content)
      }
    }

    cmd.execute(context, req.createResponse())
  }
}
