package com.sksamuel.exts

import org.scalatest.{FunSuite, Matchers}

class EitherImplicitsTest extends FunSuite with Matchers {

  import EitherImplicits._

  test("rich either should support .getOrError") {
    Right("sammy").getOrError("boom") shouldBe "sammy"
    intercept[RuntimeException] {
      Left("sammy").getOrError("boom")
    }
  }
}
