package com.sksamuel.exts.concurrent

import java.util.concurrent.{CountDownLatch, TimeUnit}

import org.scalatest.{FunSuite, Matchers}

import scala.util.{Failure, Success}

class CancellableTest extends FunSuite with Matchers {

  test("cancellable should interrupt blocking operation") {
    val latch = new CountDownLatch(1)
    val canc = Cancellable.blocking[Any](_ => latch.countDown()) {
      Thread.sleep(100000)
      "ok"
    }
    canc.cancel()
    latch.await(1, TimeUnit.MINUTES) shouldBe true
  }

  test("cancellable should invoke oncomplete with a Success(t) if the operation completes successfully") {
    val latch = new CountDownLatch(1)
    Cancellable.blocking[Any] {
      case Failure(_) =>
      case Success(_) => latch.countDown()
    } {
      Thread.sleep(1000)
      "ok"
    }
    latch.await(1, TimeUnit.MINUTES) shouldBe true
  }

  test("cancellable should invoke oncomplete with a Failure(t: InterruptedException) if the operation is interrupted") {
    val latch = new CountDownLatch(1)
    val canc = Cancellable.blocking[Any] {
      case Failure(t: InterruptedException) => latch.countDown()
      case Success(_) =>
    } {
      Thread.sleep(100000)
      "ok"
    }
    canc.cancel()
    latch.await(1, TimeUnit.MINUTES) shouldBe true
  }

  test("cancellable should invoke oncomplete with a Failure(t) if the operation fails") {
    val latch = new CountDownLatch(1)
    Cancellable.blocking[Any] {
      case Failure(_) => latch.countDown()
      case Success(_) =>
    } {
      sys.error("Boom")
      "ok"
    }
    latch.await(1, TimeUnit.MINUTES) shouldBe true
  }
}
