package com.sksamuel.exts.collection

import org.scalatest.{FunSuite, Matchers}

class MapsTest extends FunSuite with Matchers {

  test("Maps.flatten should merge keys") {
    val map = Map("a" -> "b", "c" -> Map("d" -> "e", "f" -> "g"))
    Maps.flatten(map) shouldBe Map("a" -> "b", "c.d" -> "e", "c.f" -> "g")
  }

  test("Maps.flatten should perserve values") {
    val map = Map("a" -> 5, "c" -> Map("d" -> 7, "f" -> 9))
    Maps.flatten(map) shouldBe Map("a" -> 5, "c.d" -> 7, "c.f" -> 9)
  }

  test("Maps.flatten should use separator") {
    val map = Map("a" -> "b", "c" -> Map("d" -> "e", "f" -> "g"))
    Maps.flatten(map, "-") shouldBe Map("a" -> "b", "c-d" -> "e", "c-f" -> "g")
  }
}
