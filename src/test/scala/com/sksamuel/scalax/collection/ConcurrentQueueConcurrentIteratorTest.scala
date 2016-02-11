package com.sksamuel.scalax.collection

import java.util.concurrent.ConcurrentLinkedQueue

import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class ConcurrentQueueConcurrentIteratorTest extends WordSpec with Matchers {

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
