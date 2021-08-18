package com.sksamuel.exts.net

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UrlParamParserTest extends AnyWordSpec with Matchers {

  "UrlParamParser" should {
    "parse params" in {
      UrlParamParser("?a=b&c=d") shouldBe Map("a" -> List("b"), "c" -> List("d"))
    }
    "parse single ? as empty map" in {
      UrlParamParser("?") shouldBe Map.empty
    }
    "support multi params" in {
      UrlParamParser("a=b&c=d&a=z") shouldBe Map("a" -> List("b", "z"), "c" -> List("d"))
    }
  }
}
