package com.sksamuel.exts.concurrent

import java.util.concurrent.{CountDownLatch, TimeUnit}

import org.scalatest.{FunSuite, Matchers}

import scala.util.{Failure, Success}

class CancellableTest extends FunSuite with Matchers {

  import scala.concurrent.ExecutionContext.Implicits.global

  test("cancellable should interrupt blocking operation") {
    val latch = new CountDownLatch(1)
    val canc = Cancellable {
      try {
        Thread.sleep(10000)
      } finally {
        latch.countDown()
      }
    }
    // just make sure the thread has started
    Thread.sleep(2000)
    canc.cancel()
    latch.await(10, TimeUnit.SECONDS) shouldBe true
  }

  test("cancellable should invoke oncomplete with a Success(t) if the operation completes successfully") {

    val canc = Cancellable {
      Thread.sleep(1000)
    }

    val latch = new CountDownLatch(1)
    canc.future.onComplete {
      case Failure(_) =>
      case Success(_) => latch.countDown()
    }

    latch.await(10, TimeUnit.SECONDS) shouldBe true
  }

  test("cancellable should invoke oncomplete with a Failure(t) if the operation is interrupted") {

    val canc = Cancellable {
      Thread.sleep(100000)
    }

    val latch = new CountDownLatch(1)
    canc.future.onComplete {
      case Failure(_) => latch.countDown()
      case Success(_) =>
    }

    canc.cancel()
    latch.await(10, TimeUnit.SECONDS) shouldBe true
  }

  test("cancellable should invoke oncomplete with a Failure(t) if the operation fails") {
    val canc = Cancellable {
      sys.error("Boom")
      "ok"
    }
    val latch = new CountDownLatch(1)
    canc.future.onComplete {
      case Failure(_) => latch.countDown()
      case Success(_) =>
    }
    latch.await(10, TimeUnit.SECONDS) shouldBe true
  }

  test("complete should be invoked if added after the operation completes") {
    val canc = Cancellable {
      "ok"
    }
    val latch = new CountDownLatch(1)
    canc.future.onComplete {
      case Failure(_) =>
      case Success(_) => latch.countDown()
    }
    latch.await(10, TimeUnit.SECONDS) shouldBe true
  }
}
