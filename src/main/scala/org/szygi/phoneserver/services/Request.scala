package org.szygi.phoneserver.services

trait Request {
  def number: String
  def serviceId: String
  def action: String
  def contextId: Option[Int]
  def params: List[String]
  def content: String
  def createResponse(): Response
}
