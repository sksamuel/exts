import com.typesafe.sbt.pgp.PgpKeys
import sbt._
import sbt.Keys._

object Build extends Build {

  val org = "com.sksamuel.scalax"

  val ScalaVersion = "2.11.7"
  val ScalatestVersion = "3.0.0-M12"
  val Slf4jVersion = "1.7.12"
  val Log4jVersion = "1.2.17"

  val rootSettings = Seq(
    organization := org,
    scalaVersion := ScalaVersion,
    crossScalaVersions := Seq(ScalaVersion, "2.10.6", "2.12.0-M3"),
    publishMavenStyle := true,
    resolvers += Resolver.mavenLocal,
    publishArtifact in Test := false,
    parallelExecution in Test := false,
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    javacOptions := Seq("-source", "1.7", "-target", "1.7"),
    sbtrelease.ReleasePlugin.autoImport.releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    sbtrelease.ReleasePlugin.autoImport.releaseCrossBuild := true,
    libraryDependencies ++= Seq(
      "org.slf4j"                 % "slf4j-api"        % "1.7.16",
      "org.scalatest"             %% "scalatest"       % ScalatestVersion % "test",
      "org.slf4j"                 % "slf4j-log4j12"    % Slf4jVersion     % "test",
      "log4j"                     % "log4j"            % Log4jVersion     % "test"
    ),
    publishTo <<= version {
      (v: String) =>
        val nexus = "https://oss.sonatype.org/"
        if (v.trim.endsWith("SNAPSHOT"))
          Some("snapshots" at nexus + "content/repositories/snapshots")
        else
          Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    pomExtra := {
      <url>https://github.com/sksamuel/scalax</url>
        <licenses>
          <license>
            <name>MIT</name>
            <url>https://opensource.org/licenses/MIT</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:sksamuel/scalax.git</url>
          <connection>scm:git@github.com:sksamuel/scalax.git</connection>
        </scm>
        <developers>
          <developer>
            <id>sksamuel</id>
            <name>sksamuel</name>
            <url>http://github.com/sksamuel</url>
          </developer>
        </developers>
    }
  )

  lazy val root = Project("scalax", file("."))
    .settings(rootSettings: _*)
    .settings(publish := {})
    .settings(name := "scalax")
}
