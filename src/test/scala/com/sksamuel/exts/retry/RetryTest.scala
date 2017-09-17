package com.sksamuel.exts.retry

import org.scalatest.{FunSuite, Matchers}
import scala.concurrent.duration._

class RetryTest extends FunSuite with Matchers with Retry {
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
