package com.sksamuel.scalax.metrics

import com.sksamuel.scalax.Logging

import scala.concurrent.duration._

trait Timed extends Logging {

  def timed[T](message: String)(thunk: => T): T = {
    val start = System.nanoTime()
    val t = thunk
    val end = System.nanoTime()
    val duration = (end - start).nanos
    logger.debug(s"$message took ${duration.toNanos} nanos, ${duration.toMillis} ms ${duration.toSeconds} secs")
    t
  }
}
