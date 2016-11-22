package com.sksamuel.exts.config

import java.nio.file.Paths

import com.sksamuel.exts.Logging
import com.sksamuel.exts.OptionImplicits._
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Supports environment and application config in an opinionated way.
  *
  * The application config is loaded from the name specified in the classpath.
  * Eg if the app name is "foo" then the application conf is foo.conf and this must
  * exist on the classpath at /foo.conf
  *
  * The environment config is loaded from a file called $env.conf where env is determined
  * from a sys property called CONFIG_ENV. If no env is found, it will default to LOCAL.
  * The $env.conf file will be searched for the following places, with the first match winning:
  * - current working directory (cwd)
  * - users home folder
  * - classpath
  *
  * Finally an override.conf can be located in the cwd only.
  *
  * The order of priority, highest at the top:
  * ./override.conf
  * ./$env.conf or ~/$env.conf or classpath:$env.conf
  * classpath:$application.conf
  * classpath:$reference.conf (all reference.conf files on the classpath are merged into a single file)
  */
object ConfigLoader extends Logging {

  val ConfigKey = "CONFIG_ENV"
  val LocalEnv = "LOCAL"

  def apply(appName: String = "application"): Config = {
    val env = Option(sys.props(ConfigKey)).orElse(sys.env.get(ConfigKey)).getOrElse(LocalEnv)
    val refConf = ConfigFactory.defaultReference()
    val appConf = ConfigFactory.parseResources(s"$appName.conf")
    val envConf = locateAndLoadEnvConf(env)
    val overConf = locateAndLoadOverrideConf()
    overConf.withFallback(envConf).withFallback(appConf).withFallback(refConf).resolve()
  }

  def locateAndLoadOverrideConf(): Config = {
    val overrideConfPath = Paths.get(".").resolve("override.conf").toAbsolutePath
    overrideConfPath.some.map { path =>
      logger.info(s"Loading OVERRIDE:$path")
      ConfigFactory.parseFile(path.toFile)
    }.getOrElse {
      logger.info(s"No override conf located at $overrideConfPath")
      ConfigFactory.empty()
    }
  }

  def locateAndLoadEnvConf(env: String): Config = {
    val confFilename = s"$env.conf"

    // 1st place to check is the cwd
    val cwdConfFile = Paths.get(".").resolve(confFilename).toAbsolutePath.some

    // 2nd place to look is in the users home folder
    val homeFolderConfFile = Paths.get(sys.props("user.home")).resolve(confFilename).toAbsolutePath.some

    cwdConfFile.orElse(homeFolderConfFile).map { path =>
      logger.info(s"Loading ENV:$env config from $path")
      ConfigFactory.parseFile(path.toFile)
    }.getOrElse {
      logger.info(s"Loading ENV:$env config from classpath (if present)")
      ConfigFactory.parseResources("/" + confFilename)
    }
  }
}
