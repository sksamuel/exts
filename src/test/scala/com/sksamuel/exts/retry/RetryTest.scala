package com.sksamuel.exts.retry

import scala.concurrent.duration._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RetryTest extends AnyFunSuite with Matchers with Retry {
  test("simple retry should loop max number of times") {
    var count = 0
    intercept[RuntimeException] {
      simpleRetry(5, 1.second) {
        count = count + 1
        sys.error("boom")
      }
    }
    count shouldBe 5
  }
}
