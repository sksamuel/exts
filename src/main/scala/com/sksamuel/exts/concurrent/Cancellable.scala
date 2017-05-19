package com.sksamuel.exts.concurrent

import java.util.concurrent.CountDownLatch

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}

// allows blocking operations to be cancelled (interrupted)
trait Cancellable[T] {
  def cancel(): Unit
  def block(): Unit
  def onComplete(fn: Try[T] => Any): Unit
}

object Cancellable {

  private def blocking[T <: Any](cancelfn: Thread => Any, thunk: => T): Cancellable[T] = {

    // the latch is used to allow other threads to block on this cancellable
    val latch = new CountDownLatch(1)

    val listeners = ListBuffer.empty[Try[T] => Any]

    @volatile var result: Try[T] = null

    // we want to run the thunk in its own thread, so we can block that thread and not the caller
    val thread = new Thread(new Runnable {
      // we can't use Try.apply here as it won't catch interrupted exceptions
      override def run(): Unit = {
        val t: Try[T] = try {
          Success(thunk)
        } catch {
          case t: Throwable => Failure(t)
        } finally {
          latch.countDown()
        }
        latch.synchronized {
          result = t
          listeners.foreach(_.apply(t))
        }
      }
    })
    thread.start()

    new Cancellable[T] {
      override def cancel(): Unit = cancelfn(thread)
      override def block(): Unit = latch.await()
      override def onComplete(fn: (Try[T]) => Any): Unit = latch.synchronized {
        if (result == null)
          listeners.append(fn)
        else
          fn(result)
      }
    }
  }

  def blocking[T <: Any](thunk: => T): Cancellable[T] = {
    val fn: Thread => Any = thread => thread.interrupt()
    blocking(fn, {
      thunk
    })
  }

  def blocking[T <: Any](cancelfn: => Any, thunk: => T): Cancellable[T] = {
    val fn: Thread => Any = _ => cancelfn
    blocking(fn, {
      thunk
    })
  }
}
