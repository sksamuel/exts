package com.sksamuel.exts.collection

import org.scalatest.{Matchers, WordSpec}

class CollectionsTest extends WordSpec with Matchers {

  import Collections._

  "Collections" should {
    "distinct by" in {
      val seq = Seq("1", "2", "10", "11")
      seq.distinctBy(_.length) shouldBe Seq("1", "10")
    }
    "flat collect" in {
      val seq = Seq(1, 2, 3)
      seq.flatCollect {
        case 1 => Seq("a")
        case 2 => Seq("b", "b")
        case 3 => Nil
      } shouldBe Seq("a", "b", "b")
    }
  }
}
