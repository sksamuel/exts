package com.sksamuel.exts.retry

import scala.concurrent.duration.Duration

trait Retry {
  def retry[T](strategy: RetryStrategy)(thunk: => T): T = strategy.run {
    thunk
  }

  def simpleRetry[T](attempts: Int, delay: Duration)(thunk: => T): T = {
    retry(SimpleRetryStrategy(attempts, delay)) {
      thunk
    }
  }
}

trait RetryStrategy {
  def run[T](thunk: => T): T
}

case class SimpleRetryStrategy(attempts: Int, delay: Duration) extends RetryStrategy {

  override def run[T](thunk: => T): T = {

    def run(triesLeft: Int, thunk: => T): T = {
      try {
        thunk
      } catch {
        case t: Throwable =>
          if (triesLeft == 0) throw t
          else {
            Thread.sleep(delay.toMillis)
            run(triesLeft - 1, {
              thunk
            })
          }
      }
    }

    run(attempts - 1, {
      thunk
    })
  }
}