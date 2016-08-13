package com.sksamuel.exts.io

import java.nio.file.Path

object RecursiveDelete {
  def apply(dir: Path): Unit = {
    assert(dir.toFile.isDirectory)
    dir.toFile.listFiles().foreach {
      case file if file.isDirectory => apply(file.toPath)
      case file => file.delete()
    }
    dir.toFile.delete()
  }
}
