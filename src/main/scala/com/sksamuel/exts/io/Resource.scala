package com.sksamuel.exts.io

import scala.io.Source

object Resource {
  def apply(path: String): Resource = new Resource {
    override def toString: String = lines.mkString("\n")
    override def lines: Seq[String] = Source.fromInputStream(getClass.getResourceAsStream(path)).getLines.toList
  }
}

trait Resource {
  def lines: Seq[String]
}