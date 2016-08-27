package com.sksamuel.exts.pagination

case class Pages(current: Page, totalPages: Int) {

  def before(k: Int): Range = scala.math.max(1, current.pageNumber - k).until(current.pageNumber)

  def after(k: Int): Range = (current.pageNumber + 1).to(scala.math.min(current.pageNumber + k, totalPages))

  /**
   * @return true if this page has a previous page without wrapping.
   *         That is simply, if this page > FirstPage
   */
  def hasPrevious: Boolean = current.pageNumber > 1

  /**
   * @return true if this page has a next page without wrapping.
   *         That is simply, if this page is not the last page.
   */
  def hasNext: Boolean = current.pageNumber < totalPages

  /**
   * @return true if this is the first page
   */
  def isFirst: Boolean = current.pageNumber == 1

  /**
   * @return true if this page is the lastpage
   */
  def isLast: Boolean = current.pageNumber == totalPages

  /**
   * @return a Page object representing the previous page.
   */
  def previous: Page = current.previous

  /**
   * @return a seq of Pages containing up to k previous pages. Eg, if this was page 4, then previous(2) would
   *         return a Seq of Pages for page 2, and page 3.
   */
  def previous(k: Int): Seq[Page] = {
    before(k).map(Page(_, current.pageSize, current.totalResults, current.numberOfResults))
  }

  /**
   * @return a Page object representing the next page.
   */
  def next: Page = current.next

  def next(k: Int): Seq[Page] = {
    after(k).map(Page(_, current.pageSize, current.totalResults, current.numberOfResults))
  }

  def range(k: Int): Seq[Page] = (previous(k) :+ current) ++ next(k)

  def isMultiple: Boolean = totalPages > 1
}

object Pages {

  /**
   * Creates a Pages where the current page is taken as the specified page, and the total number
   * of pages is calculated from that pageSize and totalResults preset in that specified page.
   */
  def apply[T](page: Page): Pages = {
    Pages(page, scala.math.ceil(page.totalResults.toDouble / page.pageSize).toInt)
  }
}
