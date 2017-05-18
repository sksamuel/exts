package com.sksamuel.exts.config

import java.nio.file.Paths

import com.sksamuel.exts.Logging
import com.sksamuel.exts.OptionImplicits._
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Supports environment and application config for microservices in an opinionated way.
  *
  * Starting from the lowest priority to the highest:
  *
  * - all reference.confs are loaded from the classpath and merged; the order is not defined.
  *
  * - an app_exts.conf is loaded from the classpath and applied on top of the merged reference conf files;
  * this file is meant to contain microservice specific overrides for configuration;
  * config that is required for a microservice but won't necessarily change between environments.
  *
  * - next is an environment wide config file 'env_exts.conf'
  * this should be located in the process owner's home folder or can be ommitted;
  * this file is for anything specific to the box, eg hostnames, or paths to installed binaries;
  *
  * - lastly, the application specific override config 'override.conf'
  * this should be located in the cwd of the application or can be ommited;
  * it contains config specific to the application at runtime.
  * This is useful to temporarily override values for a specific microservice.
  */
object ConfigResolver extends Logging {

  private val EnvConfFilename = "env_exts.conf"
  private val OverrideFilename = "override.conf"
  private val AppConfFilename = "app_exts.conf"

  private val userHome = Paths get System.getProperty("user.home")
  private val cwd = Paths.get(".")

  private def loadEnvConf(): Config = {
    val path = userHome.resolve(EnvConfFilename).toAbsolutePath
    logger.info(s"Detecting env conf [$path]")
    path.option.fold(ConfigFactory.empty)(it => ConfigFactory.parseFile(it.toFile))
  }

  private def loadOverrideConf(): Config = {
    val path = cwd.resolve(OverrideFilename).toAbsolutePath
    logger.info(s"Detecting override conf [$path]")
    path.option.fold(ConfigFactory.empty)(it => ConfigFactory.parseFile(it.toFile))
  }

  def apply(): Unit = {
    val refconf = ConfigFactory.defaultReference()
    val appconf = ConfigFactory.parseResources(AppConfFilename)
    loadOverrideConf().withFallback(loadEnvConf()).withFallback(appconf).withFallback(refconf)
  }
}
