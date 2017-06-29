package com.sksamuel.exts

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object TryOrLog extends Logging {
  def apply[T](r: => T): Try[T] =
    try {
      Success(r)
    } catch {
      case NonFatal(e) =>
        logger.error(e.getMessage, e)
        Failure(e)
    }
}