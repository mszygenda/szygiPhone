package org.szygi.phoneserver.services.facebook

import org.szygi.phoneserver.services.ServiceContext
import org.szygi.phoneserver.services.facebook.api.FacebookApi
import java.util.{Calendar, Date}
import scala.collection.immutable.HashMap

class FacebookServiceContext extends ServiceContext {
  val api: FacebookApi = new FacebookApi()
  val groups = api.groups
  var sendNotifications = false
  var watchedObjects: List[FacebookObject] = List()
  var watchedObjectAccessTime: HashMap[FacebookObject, Date] = HashMap()
  var notificationWatcher: Option[PeriodicCommandInvoker] = None
  var inboxWatcher: Option[PeriodicCommandInvoker] = None

  def registerObject(obj: FacebookObject) = {
    watchedObjects.zipWithIndex.find(_._1.equals(obj)) match {
      case Some((_, objRef)) =>
        objRef
      case None => {
        watchedObjects = watchedObjects :+ obj

        (watchedObjects.length - 1).toString()
      }
    }
  }

  def getObjectAccessTime(obj: FacebookObject) = {
    watchedObjectAccessTime.getOrElse(obj, {
      val zeroDate = Calendar.getInstance().getTime()

      zeroDate.setYear(70)
      zeroDate
    })
  }

  def updateAccessTime(obj: FacebookObject) {
    val now = Calendar.getInstance().getTime()

    watchedObjectAccessTime = watchedObjectAccessTime.updated(obj, now)
  }
}
