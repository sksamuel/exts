package com.sksamuel.exts.ui

// an object that contains everything needed to construct an ajax datatable and filters
case class DatatablesConfig(filters: Seq[SelectFilter],
                            columns: Seq[DatatableColumn])

case class DatatableColumn(name: String, // the header name
                           index: Int, // the position in the resultant data
                           orderable: Boolean)