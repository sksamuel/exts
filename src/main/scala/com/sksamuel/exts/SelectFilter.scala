package com.sksamuel.exts

case class SelectFilter(name: String, field: String, options: Seq[FilterOption])

case class FilterOption(value: String, label: String)