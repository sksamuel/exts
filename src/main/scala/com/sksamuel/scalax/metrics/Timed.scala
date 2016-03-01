package com.sksamuel.scalax.metrics

import com.sksamuel.scalax.Logging

import scala.concurrent.duration._

trait Timed extends Logging {

  def timed[T](thunk: => T): T = {
    val start = System.nanoTime()
    val t = thunk
    val end = System.nanoTime()
    val duration = (end - start).nanos
    logger.debug(s"Took ${duration.toNanos} nanos, ${duration.toMillis} ms ${duration.toSeconds} secs")
    t
  }
}
