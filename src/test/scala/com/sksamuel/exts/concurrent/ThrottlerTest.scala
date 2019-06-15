package com.sksamuel.exts.concurrent

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.{CountDownLatch, TimeUnit}

import org.scalatest.{FunSuite, Matchers}

class ThrottlerTest extends FunSuite with Matchers {

  test("Throttler should not execute the thunk while the counter > 0") {

    def startThread(throttler: Throttler, latch: CountDownLatch): Unit = {
      val t = new Thread(new Runnable {
        override def run(): Unit = {
          throttler {
            latch.countDown()
          }
        }
      })
      t.start()
    }

    val counter = new AtomicInteger(1)
    val lock = new ReentrantLock()
    val condition = lock.newCondition()
    val throttler = new Throttler(counter, lock, condition)

    val k = 4
    val latch = new CountDownLatch(k)
    for (_ <- 1 to k) {
      startThread(throttler, latch)
    }

    // the latch should not be triggered as counter > 1
    latch.await(2, TimeUnit.SECONDS) shouldBe false

    throttler.down()

    // now all threads should have been released
    latch.await(2, TimeUnit.SECONDS) shouldBe true
  }

}
