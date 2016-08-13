package com.sksamuel.exts.io

import java.io.{OutputStream, File}
import java.nio.file.{Files, Path}

object IO {

  def fileFromResource(resource: String): File = new File(getClass.getResource(resource).getFile)
  def pathFromResource(resource: String): Path = fileFromResource(resource).toPath

  def tempOutputStream: OutputStream = {
    val path = Files.createTempFile("io", "test")
    path.toFile.deleteOnExit()
    Files.newOutputStream(path)
  }
}
