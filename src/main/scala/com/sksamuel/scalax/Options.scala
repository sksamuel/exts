package com.sksamuel.scalax

object Options {

  /**
    * Better than Some(t) because that will return the inferred type as Some[T], but in a fold we probably want the
    * type inferred as Option[T]
    */
  implicit def some[T](t: T): Option[T] = Some(t)

  def none[T]: Option[T] = None
}
