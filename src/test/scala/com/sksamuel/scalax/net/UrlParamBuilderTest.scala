package com.sksamuel.scalax.net

import org.scalatest.{Matchers, WordSpec}

class UrlParamBuilderTest extends WordSpec with Matchers {

  "UrlParamBuilder" should {
    "build urls with = and &" in {
      UrlParamBuilder(Map("a" -> "1", "b" -> "2")) shouldBe "a=1&b=2"
    }
  }
}
