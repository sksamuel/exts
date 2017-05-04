package com.sksamuel.exts

import org.scalatest.{FunSuite, Matchers}

class SelectFilterTest extends FunSuite with Matchers {

  test("enums should fully populate from type") {
    SelectFilter[Suite] shouldBe
      SelectFilter("Suite", "suite", Seq(FilterOption("SPADES", "SPADES"), FilterOption("HEARTS", "HEARTS"), FilterOption("CLUBS", "CLUBS"), FilterOption("DIAMONDS", "DIAMONDS")))
  }
}

