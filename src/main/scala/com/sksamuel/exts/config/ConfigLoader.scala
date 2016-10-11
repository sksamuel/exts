package com.sksamuel.exts.config

import java.nio.file.Paths

import com.sksamuel.exts.Logging
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Supports environment and application config in an opinionated way.
  *
  * The environment config is loaded from a file called ${env}.conf where env is determined from a property called CONFIG_ENV.
  * If no env is found, it will fallback to LOCAL
  *
  * The application config is loaded from the name specified. Eg if the app name is "foo" then the application conf is foo.conf
  * This is expected to be located in the classpath.
  *
  * Finally an override conf can be located in the cwd only, which will be applied as first priority.
  */
object ConfigLoader extends Logging {

  val ConfigKey = "CONFIG_ENV"
  val LocalEnv = "LOCAL"

  def apply(appName: String): Config = {
    val env = Option(sys.props(ConfigKey)).orElse(sys.env.get(ConfigKey)).getOrElse(LocalEnv)
    val appConf = ConfigFactory.parseResources(s"$appName.conf")
    val envConf = locateAndLoadEnvConf(env)
    val refConf = ConfigFactory.defaultReference()
    val overConf = locateAndLoadOverrideConf()
    overConf.withFallback(envConf).withFallback(appConf).withFallback(refConf).resolve()
  }

  private def locateAndLoadOverrideConf(): Config = {
    val confFilename = "override.conf"
    val overrideConfPath = Paths.get(".").resolve(confFilename).toAbsolutePath
    if (overrideConfPath.toFile.exists) {
      logger.info(s"Loading OVERRIDE:$overrideConfPath")
      ConfigFactory.parseFile(overrideConfPath.toFile)
    } else {
      logger.info(s"No override conf located at $overrideConfPath")
      ConfigFactory.empty()
    }
  }

  private def locateAndLoadEnvConf(env: String): Config = {
    val confFilename = s"$env.conf"
    // first place to look is in the users home folder
    val homeFolderConfFile = Paths.get(sys.props("user.home")).resolve(confFilename).toAbsolutePath
    // 2nd place to check is the cwd
    val cwdConfFile = Paths.get(".").resolve(confFilename).toAbsolutePath

    if (homeFolderConfFile.toFile.exists) {
      logger.info(s"Loading ENV:$env config from $homeFolderConfFile")
      ConfigFactory.parseFile(homeFolderConfFile.toFile)
    } else if (cwdConfFile.toFile.exists) {
      logger.info(s"Loading ENV:$env config from $cwdConfFile")
      ConfigFactory.parseFile(cwdConfFile.toFile)
    } else {
      logger.info(s"Loading ENV:$env config from classpath (if present)")
      // finally we default to classpath
      ConfigFactory.parseResources(confFilename)
    }
  }
}
