package com.sksamuel.scalax.collection

import java.util.concurrent.BlockingQueue

case class BlockingQueueConcurrentIterator[E](queue: BlockingQueue[E], sentinel: E) extends Iterator[E] {
  private val iterator: Iterator[E] = {
    Iterator.continually(queue.take).takeWhile { e =>
      if (e == sentinel)
        queue.put(e)
      e != sentinel
    }
  }
  override def hasNext: Boolean = iterator.hasNext
  override def next(): E = iterator.next()
}
