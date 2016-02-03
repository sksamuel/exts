package com.sksamuel.scalax.concurrent

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
