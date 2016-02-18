package com.sksamuel.scalax.collection

import scala.collection.mutable

object Collections {

  implicit class RichSequence[T](seq: Seq[T]) {
    def distinctBy[U](f: T => U): Seq[T] = {
      val seen = mutable.HashSet[U]()
      seq.filter { t =>
        val u = f(t)
        if (seen(u)) {
          false
        } else {
          seen.add(u)
          true
        }
      }
    }
  }
}
