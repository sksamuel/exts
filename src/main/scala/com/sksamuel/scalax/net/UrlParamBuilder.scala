package com.sksamuel.scalax.net

object UrlParamBuilder {
  def apply(map: Map[String, String], separator: String = "&"): String = {
    map.map { case (key, value) => s"$key=$value" }.mkString(separator)
  }
}
