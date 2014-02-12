package org.szygi.phoneserver.services

object ServiceManager {
  private var services: Map[String, Service] = Map()
  private var contexts: Map[String, List[ServiceContext]] = Map()

  def register(service: Service) {
    services += service.id -> service
    contexts += service.id -> List(service.createContext)
  }

  def process(request: Request) = {
    val serviceId = request.serviceId
    val service = services(serviceId)

    val ctx = request.contextId match {
      case Some(ctxId) => contexts(serviceId)(ctxId)
      case None => {
        val ctx = service.createContext
        contexts updated (serviceId, contexts(serviceId) :+ ctx)

        ctx
      }
    }

    service.process(request, ctx)
  }
}
