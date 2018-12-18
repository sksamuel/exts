package com.sksamuel.exts.concurrent

import java.util.concurrent.Semaphore

trait Semaphores {

  def acquire[T](semaphore: Semaphore)(thunk: => T): T = {
    try {
      semaphore.acquire()
      thunk
    } finally {
      semaphore.release()
    }
  }

}
