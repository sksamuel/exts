package com.sksamuel.exts

import java.util.Locale

object StringImplicits {
  implicit class RicherString(string: String) {
    def isLowercase: Boolean = string.toLowerCase == string
    def isUppercase: Boolean = string.toUpperCase == string
    def isLowercase(locale: Locale): Boolean = string.toLowerCase(locale) == string
    def isUppercase(locale: Locale): Boolean = string.toUpperCase == string
  }
}
