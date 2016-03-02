package org.szygi.phoneserver.services.spotify

import org.szygi.phoneserver.services.{ServiceContext, Response}
import sys.process._

case class PlaySongCommand(number: String, title: String) {
  val playSongCmd = "tools/playMusic.sh"

  def execute(ctx: ServiceContext, response: Response) {
    println("Playing song '%s' to number '%s'" format (title, number))

    //Seq("bash", playSongCmd, number, title, "&") !

    val songPlayer = Process(Seq("bash", playSongCmd, number, title))
    songPlayer.run()
  }
}
