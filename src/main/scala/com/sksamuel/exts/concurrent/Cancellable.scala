package com.sksamuel.exts.concurrent

import scala.util.{Failure, Success, Try}

// allows blocking operations to be cancelled (interrupted)
trait Cancellable[T] {
  def cancel(): Unit
}

object Cancellable {

  // The thunk will be a blocking operation; if it's not then the operation should periodically
  // check for interrupted status, otherwise this is the wrong abstraction.
  //
  // Once the thunk completes, then the oncomplete callback will be invoked with
  // try Try wrapping the result or exception.
  //
  // If cancel is called on the returned Cancellable, then the thread will be interrupted.
  def blocking[T <: Any](oncomplete: Try[T] => Any)(thunk: => T): Cancellable[T] = {

    // we want to run the thunk in its own thread, so we can block that new thread,
    // and interrupt it if we want to cancel the op
    val thread = new Thread(new Runnable {
      // we can't use Try.apply here as it won't catch interrupted exceptions
      override def run(): Unit = {
        val t = try {
          Success(thunk)
        } catch {
          case t: Throwable => Failure(t)
        }
        oncomplete(t)
      }
    })
    thread.start()

    new Cancellable[T] {
      override def cancel(): Unit = thread.interrupt()
    }
  }
}
