package org.szygi.phoneserver.services

import org.szygi.phoneserver.services.facebook.FacebookServiceContext

trait Service {
  def id: String
  def createContext: ServiceContext
  def process(req: Request, context: ServiceContext)
}
