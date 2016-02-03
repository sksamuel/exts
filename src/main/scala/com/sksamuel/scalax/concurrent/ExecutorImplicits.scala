package com.sksamuel.scalax.concurrent

import java.util.concurrent.{Executor, ExecutorService, TimeUnit}

import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success, Try}

import scala.language.implicitConversions

object ExecutorImplicits {

  implicit class RichExecutor(executor: Executor) {
    def execute(thunk: => Unit): Unit = {
      executor.execute(new Runnable {
        override def run(): Unit = thunk
      })
    }
  }

  implicit class RichExecutorService(executor: ExecutorService) extends RichExecutor(executor) {

    def awaitTermination(timeout: Duration): Boolean = executor.awaitTermination(timeout.toNanos, TimeUnit.NANOSECONDS)

    def submit[T](thunk: => T): Future[T] = {
      val promise = Promise[T]()
      executor.execute(new Runnable {
        override def run(): Unit = {
          promise.tryComplete(Try(thunk))
        }
      })
      promise.future
    }

    def submit[T](thunk: => Unit, result: T): Future[T] = {
      val promise = Promise[T]()
      executor.execute(new Runnable {
        override def run(): Unit = {
          Try(thunk) match {
            case Success(_) => promise.trySuccess(result)
            case Failure(e) => promise.tryFailure(e)
          }
        }
      })
      promise.future
    }
  }
}

