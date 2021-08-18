package com.sksamuel.exts.collection

import java.util.concurrent.ConcurrentLinkedQueue


import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ConcurrentQueueConcurrentIteratorTest extends AnyWordSpec with Matchers {

  import scala.concurrent.ExecutionContext.Implicits.global

  "ConcurrentLinkedQueueConcurrentIterator" should {
    "provide multiple thread-safe iterators" in {
      val queue = new ConcurrentLinkedQueue[Int]
      Iterator.range(0, 10000).foreach(_ => queue.add(1))
      val futures = for ( k <- 1 to 10 ) yield Future {
        ConcurrentLinkedQueueConcurrentIterator(queue).size
      }
      val f = Future.sequence(futures)
      val total = Await.result(f, 10.seconds).sum
      total shouldBe 10000
    }
  }
}
