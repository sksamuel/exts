package com.sksamuel.scalax.concurrent

import java.util.concurrent.atomic.AtomicBoolean

import org.scalatest.{Matchers, WordSpec}

class LifecycleTest extends WordSpec with Matchers {

  "Lifecycle" should {
    "block until notified" in {

      val bool = new AtomicBoolean(false)
      val lifecycle = Lifecycle()

      import ThreadImplicits.toRunnable

      new Thread({
        lifecycle.await()
        bool.set(true)
      }).start()

      // should be false and even after 2 seconds should still be false
      // after being notified it should release the lifecycle and then set the bool to true
      bool.get() shouldBe false
      Thread.sleep(1000)
      bool.get() shouldBe false
      lifecycle.shutdown()
      Thread.sleep(1000)
      bool.get() shouldBe true
    }
  }
}
