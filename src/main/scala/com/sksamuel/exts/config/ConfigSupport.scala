package com.sksamuel.exts.config

import java.nio.file.{Path, Paths}

import com.sksamuel.exts.OptionImplicits._
import com.sksamuel.exts.collection.Maps
import com.typesafe.config.Config

import scala.collection.JavaConverters._

trait ConfigSupport {

  implicit class RichConfig(config: Config) {

    def getStringSeq(path: String): Seq[String] = if (config.hasPath(path)) config.getStringList(path).asScala else Nil
    def getIntSeq(path: String): Seq[Int] = if (config.hasPath(path)) config.getIntList(path).asScala.map(_.toInt) else Nil

    def getStringOrElse(path: String, or: String): String = if (config.hasPath(path)) config.getString(path) else or
    def getDoubleOrElse(path: String, or: Double): Double = if (config.hasPath(path)) config.getDouble(path) else or
    def getBooleanOrElse(path: String, or: Boolean): Boolean = if (config.hasPath(path)) config.getBoolean(path) else or
    def getIntOrElse(path: String, or: Int): Int = if (config.hasPath(path)) config.getInt(path) else or
    def getLongOrElse(path: String, or: Long): Long = if (config.hasPath(path)) config.getLong(path) else or
    def getPathOrElse(path: String, or: Path): Path = if (config.hasPath(path)) Paths get config.getString(path) else or

    def getStringOpt(path: String): Option[String] = if (config.hasPath(path)) config.getString(path).some else None
    def getBooleanOpt(path: String): Option[Boolean] = if (config.hasPath(path)) config.getBoolean(path).some else None
    def getIntOpt(path: String): Option[Int] = if (config.hasPath(path)) config.getInt(path).some else None
    def getLongOpt(path: String): Option[Long] = if (config.hasPath(path)) config.getLong(path).some else None
    def getDoubleOpt(path: String): Option[Double] = if (config.hasPath(path)) config.getDouble(path).some else None
    def getPathOpt(path: String): Option[Path] = if (config.hasPath(path)) Paths.get(config.getString(path)).some else None

    def flattenMap: Map[String, AnyRef] = Maps.flatten(config.root().unwrapped().asScala.toMap)
  }
}