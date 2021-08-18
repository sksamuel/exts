package com.sksamuel.exts.pagination

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PageTest extends AnyWordSpec with Matchers {

  "Page.next" should {
    "return page + 1" in {
      Page(1, 10, 10).next.pageNumber shouldBe 2
      Page(2, 10, 10).next.pageNumber shouldBe 3
    }
  }

  "Page.previous" should {
    "return page - 1" in {
      Page(2, 10, 10).previous.pageNumber shouldBe 1
    }
    "return this if page is already first page" in {
      Page(1, 10, 10).previous.pageNumber shouldBe 1
    }
  }

  "Page.first" should {
    "return 1-indexed first position for pages" in {
      Page(1, 10, 20).first shouldBe 1
      Page(2, 10, 20).first shouldBe 11
    }
  }

  "Page.last" should {
    "return 1-indexed last position for pages" in {
      Page(1, 10, 20).last shouldBe 10
      Page(2, 10, 20).last shouldBe 20
    }
  }

  "Page companion last" should {
    "create last page in a result set" in {
      Page.last(20, 60).pageNumber shouldBe 3
      Page.last(8, 26).pageNumber shouldBe 4
      Page.last(1, 1).pageNumber shouldBe 1
      Page.last(0, 0).pageNumber shouldBe 1
    }
  }

  "Page.offset" should {
    "return 0-indexed offset for pages" in {
      Page(1, 10, 20).offset shouldBe 0
      Page(2, 10, 20).offset shouldBe 10
    }
  }

  "Page.to" should {
    "return 0-indexed end position for pages" in {
      Page(1, 10, 20).end shouldBe 9
      Page(2, 10, 20).end shouldBe 19
    }
  }
}
