package com.sksamuel.exts.net

// parses the ? parts of urls into a multimap
object UrlParamParser {
  def apply(str: String): Map[String, List[String]] = {
    def applyClean(str: String): Map[String, List[String]] = {
      str.split('&').map(_.split('=')).collect {
        case Array(key, value) => key -> value
      }.groupBy(_._1).map { case (key, values) => key -> values.map(_._2).toList }
    }
    applyClean(str.stripPrefix("?"))
  }
}
