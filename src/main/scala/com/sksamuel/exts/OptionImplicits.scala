package com.sksamuel.exts

import java.nio.file.{Path, Paths}

import scala.language.implicitConversions

object OptionImplicits {

  implicit class RichOption[T](option: Option[T]) {
    def getOrError(message: String): T = option.getOrElse(sys.error(message))
  }

  /**
    * Better than Some(t) because that will return the inferred type as Some[T], but in a fold we probably want the
    * type inferred as Option[T]
    */
  implicit class RichOptionImplicits[T](t: T) {
    def some: Option[T] = Some(t)
  }

  implicit class RichStringOptionImplicits(str: String) {
    def some: Option[String] = StringOption(str)
  }

  implicit class RichPathOptionImplicits(path: Path) {
    @deprecated
    def some: Option[Path] = PathOption(path)
    def option: Option[Path] = PathOption(path)
  }

  def none[T]: Option[T] = None
}

object StringOption {
  def apply(str: String): Option[String] = Option(str).filter(_.trim.nonEmpty)
}

object NonZeroInt {
  def apply(long: Long): Option[Long] = Option(long).filter(_ != 0)
}

// wraps a path and if the path doesn't exist then we get a None
object PathOption {
  def apply(path: Path): Option[Path] = Option(path).filter(_.toFile.exists())
  def apply(str: String): Option[Path] = apply(Paths.get(str))
}
