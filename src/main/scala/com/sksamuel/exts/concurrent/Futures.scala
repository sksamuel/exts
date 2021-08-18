package com.sksamuel.exts.concurrent

import java.util.concurrent.atomic.AtomicInteger

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.util.{Failure, Success}

object Futures {

  /**
    * Returns the error from the first future that fails, without waiting for the rest of the futures to complete.
    * If all futures complete successfully then the returned Future contains unit
    * If a future fails then the returned return will be marked as failure as well, with the original
    * exception set on the returned future.
    */
  def failFast[T](futures: Iterable[Future[T]])(implicit ex: ExecutionContext): Future[Unit] = {
    val promise = Promise[Unit]
    val k = new AtomicInteger(futures.size)
    futures.foreach { future =>
      future.onComplete {
        case Success(_) =>
          if (k.decrementAndGet == 0)
            promise.success(())
        case Failure(e) =>
          promise.failure(e)
      }
    }
    promise.future
  }

  def firstThrowableOf[T](futures: TraversableOnce[Future[T]])(implicit ex: ExecutionContext): Future[Option[Throwable]] = {
    // this turns all futures into futures of trys from which we can get the first failure
    val fs = futures.map { future => future.map(x => Success(x)).recover { case t => Failure(t) } }
    Future.sequence(fs.toSeq).map(_.collectFirst {
      case Failure(t) => t
    })
  }

  implicit val duration: Duration = Duration.Inf

  implicit class RichFuture[T](f: Future[T]) {

    def mapall[S](successFn: T => S, failureFn: Throwable => S)(implicit ex: ExecutionContext): Future[S] = {
      val promise = Promise[S]()
      f.andThen {
        case Success(t) => successFn(t)
        case Failure(e) => failureFn(e)
      }
      promise.future
    }

    def await(implicit duration: Duration): T = Await.result(f, duration)
  }
}
