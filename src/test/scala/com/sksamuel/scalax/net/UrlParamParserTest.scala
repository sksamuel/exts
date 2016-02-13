package com.sksamuel.scalax.net

import org.scalatest.{Matchers, WordSpec}

class UrlParamParserTest extends WordSpec with Matchers {

  "UrlParamParser" should {
    "parse params" in {
      UrlParamParser("?a=b&c=d") shouldBe Map("a" -> List("b"), "c" -> List("d"))
    }
    "support multi params" in {
      UrlParamParser("a=b&c=d&a=z") shouldBe Map("a" -> List("b", "z"), "c" -> List("d"))
    }
  }
}
