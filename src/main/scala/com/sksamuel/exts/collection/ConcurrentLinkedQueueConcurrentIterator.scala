package com.sksamuel.exts.collection

import java.util.concurrent.ConcurrentLinkedQueue

case class ConcurrentLinkedQueueConcurrentIterator[E](queue: ConcurrentLinkedQueue[E]) extends Iterator[E] {
  private val iterator: Iterator[E] = Iterator.continually(queue.poll).takeWhile(_ != null)
  override def hasNext: Boolean = iterator.hasNext
  override def next(): E = iterator.next()
}
