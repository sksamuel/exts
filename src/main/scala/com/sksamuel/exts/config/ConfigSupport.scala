package com.sksamuel.exts.config

import com.sksamuel.exts.collection.Maps
import com.typesafe.config.Config

import scala.collection.JavaConverters._

trait ConfigSupport {

  implicit class RichConfig(config: Config) {
    def getStringSeq(path: String): Seq[String] = if (config.hasPath(path)) config.getStringList(path).asScala else Nil
    def getIntSeq(path: String): Seq[Int] = if (config.hasPath(path)) config.getIntList(path).asScala.map(_.toInt) else Nil
    def getStringOrElse(path: String, or: String) = if (config.hasPath(path)) config.getString(path) else or
    def getDoubleOrElse(path: String, or: String) = if (config.hasPath(path)) config.getDouble(path) else or
    def getBooleanOrElse(path: String, or: String) = if (config.hasPath(path)) config.getBoolean(path) else or
    def getIntOrElse(path: String, or: String) = if (config.hasPath(path)) config.getInt(path) else or
    def getLongOrElse(path: String, or: String) = if (config.hasPath(path)) config.getLong(path) else or

    def flattenMap: Map[String, AnyRef] = Maps.flatten(config.root().unwrapped().asScala.toMap)
  }
}