package org.szygi.phoneserver.services.say

import org.szygi.phoneserver.services.{AddressBook, ServiceContext, Request, Service}
import org.szygi.phoneserver.NoContext

class SayService extends Service {
  def id: String = "say"

  def createContext: ServiceContext = NoContext

  def process(req: Request, context: ServiceContext): Unit = {
    val cmd = req.action match {
      case "to" => {
        req.params match {
          case recipient :: Nil => {
            val phoneNumber = AddressBook.getPhoneNumber(recipient)

            SayIt(req, phoneNumber, 0, "10", req.content, "ewa")
          }
          case recipient :: times :: Nil=> {
            val phoneNumber = AddressBook.getPhoneNumber(recipient)

            SayIt(req, phoneNumber, 0, times, req.content, "ewa")
          }
          case recipient :: delay :: times :: Nil=> {
            val phoneNumber = AddressBook.getPhoneNumber(recipient)

            SayIt(req, phoneNumber, delay.toInt, times, req.content, "ewa")
          }
          case recipient :: delay :: times :: voice :: Nil=> {
            val phoneNumber = AddressBook.getPhoneNumber(recipient)

            SayIt(req, phoneNumber, delay.toInt, times, req.content, voice)
          }
          case _ => {
            throw new Exception("Invalid parameters to say it command")
          }
        }
      }
      case _ => {
        throw new Exception("Unkown say action")
      }
    }

    cmd.execute(null)
  }
}
