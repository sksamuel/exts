package com.sksamuel.exts.concurrent

import java.util.concurrent.{CountDownLatch, TimeUnit}

import org.scalatest.{FunSuite, Matchers}

import scala.util.{Failure, Success}

class CancellableTest extends FunSuite with Matchers {

  test("cancellable should interrupt blocking operation") {
    val latch = new CountDownLatch(1)
    val canc = Cancellable.blocking(latch.countDown(), {
      Thread.sleep(100000)
      "ok"
    })
    canc.cancel()
    latch.await(10, TimeUnit.SECONDS) shouldBe true
  }

  test("cancellable should invoke oncomplete with a Success(t) if the operation completes successfully") {

    val canc = Cancellable.blocking {
      Thread.sleep(1000)
      "ok"
    }

    val latch = new CountDownLatch(1)
    canc.onComplete {
      case Failure(_) =>
      case Success(_) => latch.countDown()
    }

    latch.await(10, TimeUnit.SECONDS) shouldBe true
  }

  test("cancellable should invoke oncomplete with a Failure(t: InterruptedException) if the operation is interrupted") {

    val canc = Cancellable.blocking {
      Thread.sleep(100000)
      "ok"
    }

    val latch = new CountDownLatch(1)
    canc.onComplete {
      case Failure(t: InterruptedException) => latch.countDown()
      case Success(_) =>
    }
    canc.cancel()
    latch.await(10, TimeUnit.SECONDS) shouldBe true
  }

  test("cancellable should invoke oncomplete with a Failure(t) if the operation fails") {
    val canc = Cancellable.blocking {
      sys.error("Boom")
      "ok"
    }
    val latch = new CountDownLatch(1)
    canc.onComplete {
      case Failure(_) => latch.countDown()
      case Success(_) =>
    }
    latch.await(10, TimeUnit.SECONDS) shouldBe true
  }

  test("complete should be invoked if added after the operation completes") {
    val canc = Cancellable.blocking {
      "ok"
    }
    val latch = new CountDownLatch(1)
    canc.onComplete {
      case Failure(_) =>
      case Success(_) => latch.countDown()
    }
    latch.await(10, TimeUnit.SECONDS) shouldBe true
  }
}
