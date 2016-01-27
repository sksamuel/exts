package com.sksamuel.scalax.io

import java.io.File
import java.nio.file.Path

object IO {

  def fileFromResource(resource: String): File = new File(getClass.getResource(resource).getFile)
  def pathFromResource(resource: String): Path = fileFromResource(resource).toPath

}
