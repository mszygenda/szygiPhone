package org.szygi.phoneserver.services.facebook.api

import collection.JavaConversions._

import facebook4j._
import facebook4j.auth.AccessToken
import java.util.Date
import scala.Some

class FacebookApi {
  val facebook = new FacebookFactory().getInstance();
  facebook.setOAuthAppId("FACEBOOK_APP_ID", "FACEBOOK_APP_SECRET")
  facebook.setOAuthPermissions("read_stream,read_friendlists,read_mailbox,user_online_presence,xmpp_login,friends_online_presence,publish_actions,publish_stream,user_groups,user_status,manage_notifications,read_requests,publish_actions,user_likes,friends_likes,user_photos,friends_photos,friends_status,read_insights,create_event,manage_friendlists,rsvp_event,email,friends_activities,user_activities,friends_checkins,user_checkins,friends_events,user_events,friends_notes,user_notes,friends_relationships,user_relationships,user_actions.music,user_actions.video,user_actions.news,manage_pages,publish_stream,friends_groups,user_relationship_details,friends_relationship_details,friends_subscriptions,user_subscriptions,ads_management,ads_read,create_event,create_note,email,export_stream,friends_about_me,friends_activities,friends_birthday,friends_checkins,friends_education_history,friends_events,friends_games_activity,friends_groups,friends_hometown,friends_interests,friends_likes,friends_location,friends_notes,friends_online_presence,friends_photo_video_tags,friends_photos,friends_questions,friends_relationship_details,friends_relationships,friends_religion_politics,friends_status,friends_subscriptions,friends_videos,friends_website,friends_work_history,manage_friendlists,manage_notifications,manage_pages,offline_access,photo_upload,publish_actions,publish_checkins,publish_stream,read_friendlists,read_insights,read_mailbox,read_page_mailboxes,read_requests,read_stream,rsvp_event,share_item,sms,status_update,user_about_me,user_activities,user_birthday,user_checkins,user_education_history,user_events,user_friends,user_games_activity,user_groups,user_hometown,user_interests,user_likes,user_location,user_notes,user_online_presence,user_photo_video_tags,user_photos,user_questions,user_relationship_details,user_relationships,user_religion_politics,user_status,user_subscriptions,user_videos,user_website,user_work_history,video_upload,xmpp_login")
  facebook.setOAuthAccessToken(new AccessToken("FACEBOOK_AUTH_TOKEN"))

  def sendMessage(conversationId: Long, message: String) {
    val msg: String = "%s sent from szygiPhone" format (message)
  }

  def createPost(post: String) = {
    val res = facebook.postStatusMessage(post)
  }

  def groups = {
    facebook.getGroups().toList
  }

  def postOnGroup(groupId: String, post: String) = {
    println("Posting message on group <%s> content: <%s>" format(groupId, post))

    facebook.postGroupFeed(groupId, new PostUpdate(post))
  }

  def notifications = {
    //new Reading().fields("object", "id", "from", "to", "created_time", "updated_time", "title", "link", "application", "unread")
    facebook.getNotifications().toList
  }

  def getNotificationsAndMarkAsReaded(): List[Notification] = {
    val notificationList = notifications

    notificationList.foreach(markAsRead(_))

    return notificationList
  }

  def getNewMessagesAndMarkAsReaded(): List[Message] = {
    val messages = inbox

    messages.foreach(markAsRead(_))
    val fullMessages = messages.map(m => facebook.getMessage(m.getId())).toList

    fullMessages.sortBy(m => {
      if (m.getCreatedTime() != null)
        m.getCreatedTime()
      else
        m.getUpdatedTime()
    })
  }

  def inbox = {
    facebook.getInbox().toList.filterNot(m => {
      _readMessages.exists(_.getId() == m.getId()) ||
      m.getTo().size() > 2 ||
      m.getUnread() == 0 ||
      m.getUnseen() == 0
    })
  }

  def markAsRead(msg: Message) {
    _readMessages = msg :: _readMessages
  }

  private var _readMessages: List[Message] = Nil

  def markAsRead(notification: Notification) {
    facebook.markNotificationAsRead(notification.getId())
  }

  private def extractObjectIdFromLink(link: String): Option[String] = {
    link.stripSuffix("/") match {
      case null => None
      case "" => None
      case str: String => Some(str.split("/").last)
    }
  }

  def like(objectId: String, objectType: ObjectType) {
    objectType match {
      case Post =>
        facebook.likePost(objectId)
      case Comment =>
        facebook.likeComment(objectId)
    }
  }

  def getGroupPosts(groupId: String, offset: Int, amount: Int) = {
    val pagingOptions = new Reading().offset(offset).limit(amount + 1)

    facebook.getGroupFeed(groupId, pagingOptions).toList
  }

  def getPostComments(postId: String, offset: Int, amount: Int) = {
    val pagingOptions = new Reading().offset(offset).limit(amount + 1)

    facebook.getPostComments(postId, pagingOptions).toList
  }

  def getPostCommentsSince(postId: String, date: Date) = {
    val pagingOptions = new Reading().since(date)

    facebook.getPostComments(postId, pagingOptions).toList
  }

  def commentPost(postId: String, comment: String) = {
    facebook.commentPost(postId, comment)
  }

  def getWall(offset: Int, amount: Int) = {
    val pagingOptions = new Reading().offset(offset).limit(amount + 1)

    facebook.getHome(pagingOptions).toList
  }

  def postStatus(content: String) = {
    facebook.postStatusMessage(content)
  }

  def removeObject(objId: String, objType: ObjectType) {
    objType match {
      case Post => {
        facebook.deletePost(objId)
      }
      case Comment => {
        facebook.deleteComment(objId)
      }
    }
  }
}
