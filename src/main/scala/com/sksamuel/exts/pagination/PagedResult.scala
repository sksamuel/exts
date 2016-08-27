package com.sksamuel.exts.pagination

/**
 * A PagedResult is a collection of objects T along with a Page instance that represents
 * which page the objects T represent.
 *
 * @param results the objects contained in the page
 * @param page page details
 **/
case class PagedResult[T](results: Seq[T], page: Page)

object PagedResult {

  /**
   * Returns an empty PagedResult which has pageNumber 1, the default page size and 0 total results
   */
  def empty[T]: PagedResult[T] = apply(Nil, Page.FirstPage, Page.DefaultPageSize, 0)

  /**
   * Creates an empty PagedResults which has pageNumber 1, 0 total results, and the pageSize is specified.
   */
  def empty[T](pageSize: Int): PagedResult[T] = apply(Nil, Page.FirstPage, pageSize, 0)

  /**
   * Creates a PagedResult from the given Iterable, where the pageSize is taken as the iterable's size,
   * pageNumber is assumed as FirstPage, and totalResults is assumed as the iterable's size.
   */
  def apply[T](iterable: Iterable[T]): PagedResult[T] = apply(iterable, iterable.size)

  /**
   * Creates a PagedResult from the given Iterable, where the pageSize is taken as the iterable's size,
   * pageNumber is assumed as FirstPage, and totalResults is specified.
   */
  def apply[T](iterable: Iterable[T], totalResults: Int): PagedResult[T] = {
    apply(iterable, Page.FirstPage, iterable.size, totalResults)
  }

  /**
   * Creates a PagedResult from a given Iterable, using the specified values
   * for the pageNumber, pageSize and totalResults.
   */
  def apply[T](iterable: Iterable[T],
               pageNumber: Int,
               pageSize: Int,
               totalResults: Int): PagedResult[T] = {
    PagedResult(iterable.toSeq, Page(pageNumber, pageSize, totalResults, iterable.size))
  }
}