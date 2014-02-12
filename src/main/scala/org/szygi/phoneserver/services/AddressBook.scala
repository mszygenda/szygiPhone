package org.szygi.phoneserver.services

import scala.collection.immutable.HashMap

object AddressBook {
  val addressBook = HashMap(
    "andrzej" -> "+48XXXXXXXXX",
    "mati" -> "+48XXXXXXXXX",
    "mathie" -> "+48XXXXXXXXX",
    "przykuc" -> "+48XXXXXXXXX",
    "hunter" -> "+48XXXXXXXXX",
    "ronin" -> "+48XXXXXXXXX",
    "putin" -> "+48XXXXXXXXX",
    "banan" -> "+48XXXXXXXXX",
    "kret" -> "+48XXXXXXXXX",
    "marek" -> "+48XXXXXXXXX",
    "marcus" -> "+48XXXXXXXXX",
    "marcin" -> "+48XXXXXXXXX",
    "dorian" -> "+48XXXXXXXXX",
    "czarna" -> "+48XXXXXXXXX",
    "me" -> "+48XXXXXXXXX",
    "szygi" -> "+48XXXXXXXXX",
    "ja" -> "+48XXXXXXXXX"
  )

  def getPhoneNumber(alias: String) = {
    val number = addressBook.getOrElse(alias, alias)

    if (!number.startsWith("+48")) {
      "+48" + number
    } else {
      number
    }
  }
}
