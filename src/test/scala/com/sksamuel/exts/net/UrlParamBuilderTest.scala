package com.sksamuel.exts.net

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UrlParamBuilderTest extends AnyWordSpec with Matchers {

  "UrlParamBuilder" should {
    "build urls with = and &" in {
      UrlParamBuilder(Map("a" -> "1", "b" -> "2")) shouldBe "a=1&b=2"
    }
  }
}
