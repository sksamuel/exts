package com.sksamuel.scalax.concurrent

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Success, Failure}

object Futures {
  implicit class RichFuture[T](f: Future[T]) {
    def mapall[S](successFn: T => S, failureFn: Throwable => S)(implicit ex: ExecutionContext): Future[S] = {
      val promise = Promise[S]()
      f.andThen {
        case Success(t) => successFn(t)
        case Failure(e) => failureFn(e)
      }
      promise.future
    }
  }
}
