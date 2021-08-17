package com.sksamuel.exts.ui

case class SelectFilter(name: String, field: String, options: Seq[FilterOption])

object SelectFilter {

  def apply[E <: Enum[E] : Manifest](name: String, field: String): SelectFilter = {
    SelectFilter(name, field, manifest.runtimeClass.getEnumConstants.map(e => FilterOption.apply(e.asInstanceOf[E])))
  }

  def apply[E <: Enum[E] : Manifest]: SelectFilter = {
    apply(
      manifest[E].runtimeClass.getSimpleName,
      manifest[E].runtimeClass.getSimpleName.take(1).toLowerCase + manifest[E].runtimeClass.getSimpleName.drop(1)
    )
  }
}

case class FilterOption(value: String, label: String)

object FilterOption {
  def apply[E <: Enum[E]](`enum`: E): FilterOption = FilterOption(`enum`.name, `enum`.name)
}