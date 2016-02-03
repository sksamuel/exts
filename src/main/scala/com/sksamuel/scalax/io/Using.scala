package com.sksamuel.scalax.io

import scala.language.reflectiveCalls

trait Using {

  def using[T, U <: {def close() : Unit}](closeable: U)(f: U => T): T = {
    try {
      f(closeable)
    } finally {
      closeable.close()
    }
  }
}
