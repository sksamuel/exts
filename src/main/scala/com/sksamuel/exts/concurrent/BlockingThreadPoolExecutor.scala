package com.sksamuel.exts.concurrent

import java.util.concurrent._
import java.util.concurrent.atomic.AtomicBoolean

/**
  * Creates an ExecutionService which will block on submit once the specified queue size has been reached.
  * The submitting thread will be unblocked once a task that is executing has completed.
  *
  * @param poolSize  sets the number of threads in the thread pool
  * @param queueSize the maximum number of _waiting_ threads. Be clear here, the queue size does not
  *                  include executing threads, but only the number of tasks that can be queued after that.
  *                  If you want an ExecutionService that blocks as soon as each thread is busy, set
  *                  queueSize to zero.
  */
class BlockingThreadPoolExecutor(poolSize: Int, queueSize: Int)
  extends ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue[Runnable]())
    with AutoCloseable {

  val semaphore = new Semaphore(poolSize + queueSize)
  val running = new AtomicBoolean(true)

  def execute(task: => Any): Unit = execute(new Runnable {
    override def run(): Unit = task
  })

  override def execute(runnable: Runnable): Unit = {
    require(running.get, "This executor has shutdown, cannot accept anymore tasks")

    var acquired = false
    while (!acquired) {
      try {
        semaphore.acquire()
        acquired = true
      } catch {
        case e: InterruptedException =>
      }
    }

    try {
      super.execute(runnable)
    } catch {
      case e: RejectedExecutionException =>
        semaphore.release()
        throw e;
    }
  }

  override def afterExecute(r: Runnable, t: Throwable): Unit = {
    super.afterExecute(r, t)
    semaphore.release()
  }

  override def close(): Unit = shutdown()

  override def shutdown(): Unit = {
    running.set(false)
    super.shutdown()
  }
}