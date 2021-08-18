val org = "com.sksamuel.exts"

val ScalaVersion = "2.13.4"
val ScalatestVersion = "3.2.9"
val Slf4jVersion = "1.7.12"
val Log4jVersion = "1.2.17"

val rootSettings = Seq(
  organization := org,
  scalaVersion := ScalaVersion,
  crossScalaVersions := Seq("3.0.1", "2.13.4", "2.12.12", "2.11.12"),
  publishMavenStyle := true,
  resolvers += Resolver.mavenLocal,
  publishArtifact in Test := false,
  parallelExecution in Test := false,
  scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
  javacOptions := Seq("-source", "1.8", "-target", "1.8"),
  sbtrelease.ReleasePlugin.autoImport.releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  sbtrelease.ReleasePlugin.autoImport.releaseCrossBuild := true,
  libraryDependencies ++= Seq(
    "org.slf4j"                     % "slf4j-api"                   % "1.7.16",
    "com.typesafe"                  % "config"                      % "1.3.0",
    "org.scalatest"                 %% "scalatest"                  % ScalatestVersion      % "test",
    "org.slf4j"                     % "slf4j-log4j12"               % Slf4jVersion          % "test",
    "log4j"                         % "log4j"                       % Log4jVersion          % "test",
    "com.h2database"                % "h2"                          % "1.4.191"             % "test"
  ) ++ (if (scalaVersion.value startsWith "2.") Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value) else Nil),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (version.value.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := {
    <url>https://github.com/sksamuel/exts</url>
      <licenses>
        <license>
          <name>Apache2.0</name>
          <url>https://opensource.org/licenses/Apache-2.0</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:sksamuel/exts.git</url>
        <connection>scm:git@github.com:sksamuel/exts.git</connection>
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

lazy val root = Project("exts", file("."))
  .settings(rootSettings: _*)
  .settings(name := "exts")

