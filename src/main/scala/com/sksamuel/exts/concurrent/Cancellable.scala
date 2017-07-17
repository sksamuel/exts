package com.sksamuel.exts.concurrent

import java.util.concurrent.{Callable, Executors, FutureTask}

import scala.concurrent.{Future, Promise}
import scala.util.Try

class Cancellable[T](task: => T) {

  private val promise = Promise[T]()

  private val callable = new Callable[T] {
    override def call(): T = task
  }

  private val jf: FutureTask[T] = new FutureTask[T](callable) {
    override def done(): Unit = promise.complete(Try(get()))
  }

  private val executor = Executors.newSingleThreadExecutor()
  executor.submit(jf)
  executor.shutdown()

  def future: Future[T] = promise.future
  def cancel(): Unit = jf.cancel(true)
}

object Cancellable {
  def apply[T](task: => T): Cancellable[T] = new Cancellable[T](task)
}
