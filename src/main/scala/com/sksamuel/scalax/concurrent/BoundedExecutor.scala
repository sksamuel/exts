package com.sksamuel.scalax.concurrent

import java.util.concurrent.{Executor, Semaphore}
import ThreadImplicits.toRunnable

class BoundedExecutor(executor: Executor, bound: Int) {
  val semaphore = new Semaphore(bound)
  def submit(thunk: => Any): Unit = {
    try {
      semaphore.acquire()
      executor.execute {
        try {
          thunk
        } finally {
          semaphore.release()
        }
      }
    } finally {
      semaphore.release()
    }
  }
}