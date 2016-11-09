package com.sksamuel.exts.collection

object Maps {

  import scala.collection.JavaConverters._

  /**
    * Given a nested map of strings, will create a flatted map, where the strings are joined with a separator.
    * So given Map("a" -> "b", "c" -> Map("d" -> "e")) then the output will be
    * Map("a" -> "b", "c.d" -> "e")
    */
  def flatten[V](map: Map[String, V], separator: String = "."): Map[String, V] = map.flatMap {
    case (key, value: Map[String, V]) => flatten(value).map { case (k, v) => s"$key$separator$k" -> v }
    case (key, value: java.util.Map[String, V]) => flatten(value.asScala.toMap[String, V]).map { case (k, v) => s"$key$separator$k" -> v }
    case (key, value: V) => Map(key -> value)
  }
}
