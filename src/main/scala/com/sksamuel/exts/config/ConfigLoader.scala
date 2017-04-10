package com.sksamuel.exts.config

import java.nio.file.{Path, Paths}

import com.sksamuel.exts.Logging
import com.sksamuel.exts.OptionImplicits._
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Supports environment and application config in an opinionated way.
  *
  * Starting from the lowest priority to the highest:
  *
  * - all reference.confs are loaded from the classpath and merged; the order is not defined.
  * - application.conf is loaded from the classpath
  * - ENV.conf is loaded from working dir, user home, and classpath in that order, and merged.
  * - override.conf is loaded from working dir and user home and merged.
  *
  * Override.conf is designed to allow easy, temporary, overrides at runtime for a particular process,
  * and therefore is only ever loaded from the user home and cwd.
  *
  * The env config is designed to change between environments. The filename has a placeholder $ENV which is
  * replaced by the value of the system property called by default CONFIG_ENV.
  * If no env is found, it will default to LOCAL.
  *
  */
case class ConfigLoaderParams(overrideConfInUserHome: Boolean = true,
                              overrideConfInWorkingDir: Boolean = true,
                              envConfInWorkingDir: Boolean = true,
                              envConfInClasspath: Boolean = true,
                              envConfInUserHome: Boolean = true,
                              envConfFilename: String = "$ENV.conf",
                              applicationConfName: String = "application.conf",
                              configEnvParameterName: String = "CONFIG_ENV",
                              overrideConfName: String = "override.conf")

object ConfigLoader extends Logging {

  val ConfigKey = "CONFIG_ENV"
  val LocalEnv = "LOCAL"

  def load(params: ConfigLoaderParams = ConfigLoaderParams()): Config = {

    val env = Option(sys.props(ConfigKey)).orElse(sys.env.get(ConfigKey)).getOrElse(LocalEnv)
    val resolvedEnvConfFilename = params.envConfFilename.replace("$ENV", env)
    val userHome = System.getProperty("user.home")

    def loadPath(path: Path): Config = {
      path.toAbsolutePath.some.map { path =>
        logger.info(s"Loading path:$path")
        ConfigFactory.parseFile(path.toFile)
      }.getOrElse {
        ConfigFactory.empty()
      }
    }

    def loadResource(resource: String): Config = {
      logger.info(s"Loading classpath:$resource (if present)")
      ConfigFactory.parseResources(resource)
    }

    def overrideConf(): Config = {
      def loadOverrideFromDir(path: String) = loadPath(Paths.get(path).resolve(params.overrideConfName))
      val workingDirConf = if (params.overrideConfInWorkingDir) loadOverrideFromDir(".") else ConfigFactory.empty()
      val userHomeConf = if (params.overrideConfInUserHome) loadOverrideFromDir(userHome) else ConfigFactory.empty()
      workingDirConf.withFallback(userHomeConf)
    }

    def envConf(): Config = {
      def loadEnvConfFromDir(path: String) = loadPath(Paths.get(path).resolve(resolvedEnvConfFilename))
      val workingDirConf = if (params.envConfInWorkingDir) loadEnvConfFromDir(".") else ConfigFactory.empty()
      val userHomeConf = if (params.envConfInUserHome) loadEnvConfFromDir(userHome) else ConfigFactory.empty()
      val classpathConf = loadResource(resolvedEnvConfFilename)
      workingDirConf.withFallback(userHomeConf).withFallback(classpathConf)
    }

    val refconf = ConfigFactory.defaultReference()
    val appconf = ConfigFactory.parseResources(params.applicationConfName)
    val envconf = envConf()
    val overrideconf = overrideConf()

    overrideconf.withFallback(envconf).withFallback(appconf).withFallback(refconf).resolve()
  }

  @deprecated("use apply(params: ConfigLoaderParams)", "1.41.0")
  def apply(appName: String = "application"): Config = {

    def locateAndLoadOverrideConf(name: String): Config = {
      val overrideConfPath = Paths.get(".").resolve(name).toAbsolutePath
      overrideConfPath.some.map {
        path =>
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

      cwdConfFile.orElse(homeFolderConfFile).map {
        path =>
          logger.info(s"Loading ENV:$env config from $path")
          ConfigFactory.parseFile(path.toFile)
      }.getOrElse {
        // 3rd place to look is the classpath
        logger.info(s"Loading ENV:$env config from classpath (if present)")
        ConfigFactory.parseResources(confFilename)
      }
    }

    val env = Option(sys.props(ConfigKey)).orElse(sys.env.get(ConfigKey)).getOrElse(LocalEnv)
    val refConf = ConfigFactory.defaultReference()
    val appConf = ConfigFactory.parseResources(s"$appName.conf")
    val envConf = locateAndLoadEnvConf(env)
    val overConf = locateAndLoadOverrideConf("override.conf")
    overConf.withFallback(envConf).withFallback(appConf).withFallback(refConf).resolve()
  }
}