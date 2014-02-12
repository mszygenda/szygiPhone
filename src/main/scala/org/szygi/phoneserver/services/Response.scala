package org.szygi.phoneserver.services

trait Response {
  def abandon()
  def write(data: String)
  def close()

  def close(chunk: String) {
    write(chunk)
    close()
  }
}
