package com.sksamuel.exts

import com.sksamuel.exts.ui.{FilterOption, SelectFilter}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class SelectFilterTest extends AnyFunSuite with Matchers {

  test("enums should fully populate from type") {
    SelectFilter[Suite] shouldBe
      SelectFilter("Suite", "suite", Seq(FilterOption("SPADES", "SPADES"), FilterOption("HEARTS", "HEARTS"), FilterOption("CLUBS", "CLUBS"), FilterOption("DIAMONDS", "DIAMONDS")))
  }
}

