package com.sksamuel.exts.collection

import scala.collection.mutable

object Collections {

  implicit class RichSequence[T](seq: Seq[T]) {

    def findOrElse[U >: T](p: T => Boolean)(default: U): U = seq.find(p).getOrElse(default)

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

  implicit class RichMap[K, V](map: Map[K, V]) {
    def containsAll(keys: Seq[K]): Boolean = keys.forall(map.contains)
  }
}
