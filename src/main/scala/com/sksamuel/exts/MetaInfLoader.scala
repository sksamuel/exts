package com.sksamuel.exts

import java.util.Properties
import scala.collection.JavaConverters._

object MetaInfLoader {
  def apply(): Seq[Map[String, String]] = {
    getClass.getClassLoader.getResources("META-INF/MANIFEST.MF").asScala.map { resource =>
      val props = new Properties()
      val stream = resource.openStream()
      props.load(stream)
      props.asScala.toMap
    }.toSeq
  }
}
