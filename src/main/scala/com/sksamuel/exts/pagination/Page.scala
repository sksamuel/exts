package com.sksamuel.exts.pagination

/**
 * A Page represents a slice of objects from some collection. A collection can be
 * broken down into n pages, each with k results, with the last page containing j <=  k results.
 *
 * A Page is 1-indexed.
 *
 * @param pageNumber the page number represented by this page. 1 indexed.
 * @param pageSize the max number of results per page.
 * @param totalResults the total number of results in the underlying collection
 * @param numberOfResults the number of results in this page, which is always <= pageSize
 */
case class Page(pageNumber: Int,
                pageSize: Int,
                totalResults: Int,
                numberOfResults: Int) {
  /**
   * @return a new Page which is the next logical page.
   */
  def next: Page = copy(pageNumber = pageNumber + 1)

  /**
   * @return a new Page which is the previous logical page. If wrap is specified then previous will
   *         wrap to the last page if this is already the first page, otherwise it will return itself.
   */
  def previous: Page = if (pageNumber == Page.FirstPage) this else copy(pageNumber = pageNumber - 1)

  /**
   * @return the index of the first result on this page as a 0-indexed number.
   *         For example, offet for pages of size 20, will be 0, 20, 40, ...
   */
  def offset: Int = (pageNumber - 1) * pageSize

  /**
   * @return the index of the last result on this page as a 0-indexed number.
   *         For example, to for pages of size 20, will be 19, 39, 59, ...
   */
  def end: Int = offset + pageSize - 1

  /**
   * @return the position of the first result on this page as a 1-indexed number.
   *         For example, first for pages of size 20 will be 1, 21, 41, ...
   */
  def first: Int = offset + 1

  /**
   * @return the position of the last result on this page as 1-indexed number.
   *         For example, last for pages of size 20 will be 20, 40, 60, ....
   */
  def last: Int = offset + pageSize
}

object Page {

  val FirstPage = 1
  val DefaultPageSize = 20

  /**
   * Creates a new Page where the pageNumber is set as the first page, the numberOfResults is taken from
   * the pageSize, and the pageSize and totalResults are specified.
   */
  def first(pageSize: Int, totalResults: Int): Page = Page(FirstPage, pageSize, totalResults, pageSize)

  /**
   * Creates a new Page where the pageNumber is calcuated from the specified parameters,
   * the numberOfResults is taken from the pageSize, and the pageSize and totalResults are specified.
   */
  def last(pageSize: Int, totalResults: Int): Page = {
    val pageNumber = math.ceil(totalResults.toDouble / math.max(pageSize, 1)).toInt
    Page(math.max(1, pageNumber), pageSize, totalResults, pageSize)
  }

  /**
   * Creates a one page resultset, assuming the collection of Ts given is the entire
   * set of objects in a single page.
   */
  def apply[T](ts: Iterable[T]): Page = Page(FirstPage, ts.size, ts.size, ts.size)

  /**
   * Creates a Page that uses the given Iterable to determine the page size.
   * The total results is taken from the other parameter.
   *
   * @param ts collection used to determine page size.
   * @param totalResults total number of underlying objects in the resultset
   */
  def apply[T](ts: Iterable[T], totalResults: Int): Page = Page(FirstPage, ts.size, totalResults, ts.size)

  def apply[T](ts: Iterable[T], totalResults: Int, pageSize: Int): Page = {
    Page(FirstPage, ts.size, totalResults, pageSize)
  }
  /**
   * Creates a Page where the pageNumber, pageSize and totalResults are specified, and the numberOfResults
   * is assumed to be a full page.
   */
  def apply[T](pageNumber: Int, pageSize: Int, totalResults: Int): Page = {
    Page(pageNumber, pageSize, totalResults, pageSize)
  }

  def apply[T](pageNumber: Int, ts: Iterable[T], totalResults: Int): Page = {
    Page(pageNumber, ts.size, totalResults, ts.size)
  }
}