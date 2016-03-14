package com.sksamuel.scalax.concurrent

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Failure, Success, Try}

object Futures {

  def firstThrowableOf[T](futures: TraversableOnce[Future[T]])(implicit ex: ExecutionContext): Future[Option[Throwable]] = {
    // this turns all futures into futures of trys from which we can get the first failure
    val fs = futures.map { future => future.map(x => Success(x)).recover { case t => Failure(t) } }
    Future.sequence(fs).map(_.collectFirst {
      case Failure(t) => t
    })
  }

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
