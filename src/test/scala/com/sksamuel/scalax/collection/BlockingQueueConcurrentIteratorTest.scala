package com.sksamuel.scalax.collection

import java.util.concurrent.LinkedBlockingQueue

import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class BlockingQueueConcurrentIteratorTest extends WordSpec with Matchers {

  import scala.concurrent.ExecutionContext.Implicits.global

  "BlockingQueueConcurrentIterator" should {
    "provide multiple thread-safe iterators" in {

      val queue = new LinkedBlockingQueue[Int]
      Iterator.range(0, 10000).foreach(_ => queue.put(1))
      queue.put(-1)

      val futures = for ( k <- 1 to 10 ) yield Future {
        BlockingQueueConcurrentIterator(queue, -1).size
      }

      val f = Future.sequence(futures)
      val total = Await.result(f, 10.seconds).sum
      total shouldBe 10000
    }
    "share work between multiple iterators" in {

      val queue = new LinkedBlockingQueue[Int]
      Iterator.range(0, 100000).foreach(_ => queue.put(1))
      queue.put(-1)

      val futures = for ( k <- 1 to 10 ) yield Future {
        BlockingQueueConcurrentIterator(queue, -1).size
      }

      val f = Future.sequence(futures)
      val results = Await.result(f, 10.seconds)
      // work should have been shared
      results.count(_ >= 100) > 2
    }
  }
}
