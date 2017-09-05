package com.sksamuel.exts

object EitherImplicits {
  implicit class RichEitherImplicits[T](t: T) {
    def left[R]: Left[T, R] = Left(t)
    def right[L]: Right[L, T] = Right(t)
  }
}
