package com.sksamuel.exts

object EitherImplicits {

  implicit class EitherOps[T](t: T) {
    def left[R]: Left[T, R] = Left(t)
    def right[L]: Right[L, T] = Right(t)
  }

  implicit class RichEither[L, R](either: Either[L, R]) {
    def getOrError(message: String): R = either.right.getOrElse(sys.error(message))
  }
}
