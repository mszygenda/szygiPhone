package org.szygi.phoneserver.services

import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging
import org.szygi.phoneserver.services.spotify.SpotifyService
import org.szygi.phoneserver.services.facebook.FacebookService
import org.szygi.phoneserver.services.say.SayService

class ServerActor extends Actor {
  ServiceManager.register(new SpotifyService())
  ServiceManager.register(FacebookService)
  ServiceManager.register(new SayService())

  def receive = {
    case req: Request => {
      try {
        ServiceManager.process(req)
      } catch {
        case e: Exception => {
          println("Failed to process request %s" format req.toString())
          println(e.getStackTrace())
        }
      }
    }
    case _ =>
  }
}
