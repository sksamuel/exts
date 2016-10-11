package com.sksamuel.exts.collection

object Maps {

  /**
    * Given a nested map of strings, will create a flatted map, where the strings are joined with a separator.
    * So given Map("a" -> "b", "c" -> Map("d" -> "e")) then the output will be
    * Map("a" -> "b", "c.d" -> "e")
    */
  def flatten[V](map: Map[String, V], separator: String = "."): Map[String, V] = map.flatMap {
    case (key, value: Map[_, _]) => flatten(value.asInstanceOf[Map[String, V]]).map { case (k, v) => s"$key$separator$k" -> v }
    case (key, value) => Map(key -> value)
  }
}
