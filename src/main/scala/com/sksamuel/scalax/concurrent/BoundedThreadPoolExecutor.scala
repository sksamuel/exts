package com.sksamuel.scalax.concurrent

import java.util.concurrent._
import java.util.concurrent.atomic.AtomicBoolean

/**
  * Creates an executor which will block on submit once the specified queue size has been reached.
  */
class BoundedThreadPoolExecutor(poolSize: Int, queueSize: Int)
  extends ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue[Runnable]())
    with AutoCloseable {

  val semaphore = new Semaphore(poolSize + queueSize)
  val running = new AtomicBoolean(true)

  def execute(task: => Any): Unit = execute(new Runnable {
    override def run(): Unit = task
  })

  override def execute(runnable: Runnable): Unit = {
    while (running.get) {
      try {
        semaphore.acquire()
        try {
          super.execute(runnable)
        } catch {
          case e: RejectedExecutionException =>
            semaphore.release()
            throw e;
        }
      } catch {
        case e: InterruptedException =>
      }
    }
  }

  override def afterExecute(r: Runnable, t: Throwable): Unit = {
    super.afterExecute(r, t)
    semaphore.release()
  }

  override def close(): Unit = {
    running.set(false)
  }
}