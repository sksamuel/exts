package com.sksamuel.exts.collection

import java.util.concurrent.BlockingQueue

// Accepts a blocking queue and a sentinel value, and returns a Scala iterator.
// Allows multiple iterators to be created on the same blocking queue, hence the ConcurrentIterator name.
// The  iterator will return elements until it encounters the sentinel value. After finding the sentinel
// the iterator will keep the sentinel on the queue so that any other iterators on the same source
// can also detect it and terminate.
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


