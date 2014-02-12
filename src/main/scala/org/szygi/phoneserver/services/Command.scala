package org.szygi.phoneserver.services

trait Command {
  def execute(ctx: ServiceContext)
}
