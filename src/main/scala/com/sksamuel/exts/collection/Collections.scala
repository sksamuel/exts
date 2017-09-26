package com.sksamuel.exts.collection

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import com.sksamuel.exts.OptionImplicits._

object Collections {

  implicit class RichSequence[T](seq: Seq[T]) {

    def headOrError(msg: String): T = seq.headOption.getOrError(msg)

    def flatCollect[B](pf: PartialFunction[T, Seq[B]]): Seq[B] = {
      val builder = ListBuffer.empty[B]
      seq.foreach(pf.runWith(builder appendAll _))
      builder.result
    }

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

    def mapBy[U](fn: T => U): Map[U, T] = seq.map { t => fn(t) -> t }.toMap
  }

  implicit class RichMap[K, V](map: Map[K, V]) {
    def containsAll(keys: Seq[K]): Boolean = keys.forall(map.contains)

    def getOrError(key: K, msg: String): V = map.getOrElse(key, sys.error(msg))
  }
}
