package com.sksamuel.exts.pagination

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

/** @author Stephen Samuel */
class PagedResultTest extends AnyWordSpec with Matchers {

  "PagedResult.apply" should {
    "use results for size" in {
      val results = List("a", "b", "c")
      val result = PagedResult(results)
      result.page.pageNumber shouldBe 1
      result.page.pageSize shouldBe 3
      result.page.totalResults shouldBe 3
    }
  }
}
