package com.sksamuel.scalax

object OptionImplicits {

  /**
    * Better than Some(t) because that will return the inferred type as Some[T], but in a fold we probably want the
    * type inferred as Option[T]
    */
  implicit def some[T](t: T): Option[T] = Some(t)

  def none[T]: Option[T] = None
}

object NonEmptyString {
  def apply(str: String): Option[String] = Option(str).filter(_.trim.nonEmpty)
}

object NonZeroInt {
  def apply(long: Long): Option[Long] = Option(long).filter(_ != 0)
}
