package com.sksamuel.exts.collection

import java.util.concurrent.ConcurrentLinkedQueue

// wraps a java ConcurrentLinkedQueue in a scala Iterator.
// the iterator continues to request elements from the queue until it finds a sentinel value
case class ConcurrentLinkedQueueConcurrentIterator[E, S](queue: ConcurrentLinkedQueue[E], sentinel: S = null) extends Iterator[E] {
  private val i: Iterator[E] = Iterator.continually(queue.poll).takeWhile(_ != sentinel)
  override def hasNext: Boolean = i.hasNext
  override def next(): E = i.next()
}
