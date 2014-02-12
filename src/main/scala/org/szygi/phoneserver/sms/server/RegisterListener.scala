package org.szygi.phoneserver.sms.server

import akka.actor.ActorRef

case class RegisterListener(listener: ActorRef)