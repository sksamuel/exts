package com.sksamuel.scalax.collection

import java.util.concurrent.BlockingQueue

class BlockingQueueConcurrentIterator[E](queue: BlockingQueue[E]) extends Iterator[E] {
  private val iterator: Iterator[E] = {
    Iterator.continually(queue.take).takeWhile { e =>
      if (e == null)
        queue.put(e)
      e != null
    }
  }
  override def hasNext: Boolean = iterator.hasNext
  override def next(): E = iterator.next()
}
