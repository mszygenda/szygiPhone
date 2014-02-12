package org.szygi.phoneserver.services.facebook
import akka.actor.ActorDSL._
import akka.actor.{Cancellable, ActorSystem}
import org.szygi.phoneserver.services.facebook.commands.{FacebookCommand, GetNotifications}
import org.szygi.phoneserver.services.Request
import scala.concurrent._
import scala.concurrent.duration._

class PeriodicCommandInvoker(checkCmd: FacebookCommand, ctx: FacebookServiceContext) {
  implicit val system = ActorSystem("demo")

  private case object CheckNotifications

  private var activeScheduler: Option[Cancellable] = None

  private val tickActor = actor(new Act {
    become {
      case CheckNotifications => {
        println("Invoking command")
        checkCmd.execute(ctx)
      }
    }
  })

  def enable {
    import ExecutionContext.Implicits.global

    disable
    activeScheduler = Some(system.scheduler.schedule(500 milliseconds, 2 minutes, tickActor, CheckNotifications))
  }

  def disable {
    activeScheduler match {
      case Some(scheduler) => {
        if (!scheduler.isCancelled) {
          scheduler.cancel()
        }
      }
      case None => {
        println("Scheduler wasn't running")
      }
    }
  }
}
