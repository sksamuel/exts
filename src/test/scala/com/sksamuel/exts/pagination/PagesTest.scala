package com.sksamuel.exts.pagination

import org.scalatest.{Matchers, WordSpec}

/** @author Stephen Samuel */
class PagesTest extends WordSpec with Matchers {

  "Pages.hasNext" should {
    "be true when current < total pages" in {
      val pages = Pages(Page(1, 20, 40))
      pages.hasNext shouldBe true
    }
    "be false when current lage is last" in {
      val pages = Pages(Page.last(20, 40))
      pages.hasNext shouldBe false
    }
  }
  "Page.next" should {
    "return next page when current page < total pages" in {
      val pages = Pages(Page(2, 20, 60))
      pages.next shouldBe Page(3, 20, 60)
    }
  }

  "Pages.hasPrevious" should {
    "return true when current page > 1" in {
      val pages = Pages(Page(2, 20, 40))
      pages.hasPrevious shouldBe true
    }
    "return false when current page is first" in {
      val pages = Pages(Page.first(20, 40))
      pages.hasPrevious shouldBe false
    }
  }

  "Pages.previous" should {
    "return previous page when current page > 1" in {
      val pages = Pages(Page(2, 20, 40))
      pages.previous shouldBe Page(1, 20, 40)
    }
  }

  "Pages.isFirst" should {
    "be true when current page is 1" in {
      Pages(Page(1, 2, 10)).isFirst shouldBe true
    }
    "not true when current page > 1" in {
      Pages(Page(2, 2, 10)).isFirst shouldBe false
    }
  }

  "Pages.isLast" should {
    "be true when current page == total pages" in {
      Pages(Page(5, 2, 10)).isLast shouldBe true
    }
    "not true when current page < total pages" in {
      Pages(Page(2, 2, 10)).isLast shouldBe false
    }
  }

  "Pages.before" should {
    "be bounded by page 1" in {
      Pages(Page(4, 2, 10)).before(1000).start shouldBe 1
    }
    "respect k when returning before range" in {
      Pages(Page(5, 20, 140)).before(2).start shouldBe 3
    }
  }

  "Pages.after" should {
    "be bounded by last page" in {
      Pages(Page(2, 2, 10)).after(1000).end shouldBe 5
    }
    "respect k when returning after range" in {
      Pages(Page(2, 2, 10)).after(2).end shouldBe 4
    }
  }

  "Pages.multiple" should {
    "return true when total pages > 1" in {
      Pages(Page(1, 20, 40)).isMultiple shouldBe true

    }
    "return false when total pages == 1" in {
      Pages(Page(1, 20, 20)).isMultiple shouldBe false
    }
  }
}
