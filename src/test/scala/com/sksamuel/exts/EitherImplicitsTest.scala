package com.sksamuel.exts

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class EitherImplicitsTest extends AnyFunSuite with Matchers {

  import EitherImplicits._

  test("rich either should support .getOrError") {
    Right("sammy").getOrError("boom") shouldBe "sammy"
    intercept[RuntimeException] {
      Left("sammy").getOrError("boom")
    }
  }
}
