package org.szygi.phoneserver.services.facebook.commands

import org.szygi.phoneserver.services.Request
import org.szygi.phoneserver.services.facebook.api._

object CommandParser {
  def parse(req: Request): FacebookCommand = {
    req.action match {
      case "del" => {
        req.params match {
          case objRef :: Nil => {
            DeleteObject(objRef.toInt)
          }
          case _ => {
            throw new Exception("Invalid parameters to delete action")
          }
        }
      }
      case "post" | "p" => {
        req.params match {
          case Nil => {
            PostStatus(req, req.content)
          }
          case groupId :: Nil => {
            PostOnGroup(req, groupId.toInt, req.content)
          }
          case _ => {
            throw new Exception("Invalid parameters to post actionadm")
          }
        }
      }
      case "groups" | "g" => {
        ListGroups(req)
      }
      case "notifications" | "n" => {
        req.params match {
          case Nil => GetNotifications(req, false)
          case "1" :: Nil => GetNotifications(req, true)
          case "0" :: Nil => DisableNotifications
          case _ => throw new Exception("Invalid parameters to notifications action")
        }
      }
      case "messages" | "msg" => {
        req.params match {
          case Nil => GetInbox(req, false)
          case "1" :: Nil => GetInbox(req, true)
          case "0" :: Nil => DisableMessageNotifications
          case _ => throw new Exception("Invalid parameters to notifications action")
        }
      }
      case "like" | "l" => {
        req.params match {
          case objRef :: List() => Like(objRef.toInt, req)
          case objRef :: List() => Like(objRef.toInt, req)
          case _ => throw new Exception("Invalid parameters to like action")
        }
      }
      case "posts" => {
        req.params match {
          case groupRef :: Nil =>
            GetPosts(req, Some(groupRef.toInt), 0, 2)
          case groupRef :: amount :: Nil =>
            GetPosts(req, Some(groupRef.toInt), 0, amount.toInt)
          case groupRef :: offset :: amount :: Nil =>
            GetPosts(req, Some(groupRef.toInt), offset.toInt, amount.toInt)
          case _ =>
            throw new Exception("Invalid parameters to posts action")
        }
      }
      case "wall" => {
        req.params match {
          case Nil =>
            GetPosts(req, None, 0, 2)
          case amount :: Nil =>
            GetPosts(req, None, 0, amount.toInt)
          case offset :: amount :: Nil =>
            GetPosts(req, None, offset.toInt, amount.toInt)
          case _ =>
            throw new Exception("Invalid parameters to wall action")
        }
      }
      case "comments" | "postc" => {
        req.params match {
          case postRef :: Nil =>
            GetPostComments(req, postRef.toInt, 0, 2)
          case postRef :: amount :: Nil =>
            GetPostComments(req, postRef.toInt, 0, amount.toInt)
          case postRef :: offset :: amount:: Nil =>
            GetPostComments(req, postRef.toInt, offset.toInt, amount.toInt)
          case _ =>
            throw new Exception("Invalid parameters to GetPostComments action")
        }
      }
      case "comment" | "com" => {
        req.params match {
          case postRef :: Nil =>
            CommentPost(postRef.toInt, req.content)
          case _ =>
            throw new Exception("Invalid parameters to comment action")
        }
      }
      case "needsyou" => {
        req.params match {
          case groupId :: Nil =>
            PostOnGroup(req, groupId.toInt, "Marcus, Czarna majówka wzywa. http://pro.zapodaj.net/uploads/szygi/1/majowkaneeds.png")
          case Nil =>
            throw new Exception("Invalid parameters to needs you action")
        }
      }
    }
  }
}