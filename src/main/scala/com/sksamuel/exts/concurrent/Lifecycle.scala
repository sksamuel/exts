package com.sksamuel.exts.concurrent

trait Lifecycle {
  /**
    * Block on this lifecylce and wait until it is released.
    */
  def await(): Unit

  /**
    * Notify this lifecycle that all waiters can be released.
    */
  def shutdown(): Unit
}

object Lifecycle {
  def apply(): Lifecycle = new Lifecycle {

    @volatile var running = true
    val lock = new Object {}

    /**
      * Block on this lifecylce and wait until it is released.
      */
    override def await(): Unit = {
      while (running) {
        lock.synchronized {
          lock.wait()
        }
      }
    }

    /**
      * Notify this lifecycle that all waiters can be released.
      */
    override def shutdown(): Unit = {
      running = false
      lock.synchronized {
        lock.notifyAll()
      }
    }
  }
}
