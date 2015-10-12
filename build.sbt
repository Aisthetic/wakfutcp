
lazy val commonSettings = Seq(
  organization := "com.github.wakfutcp",
  version := "0.1.2-SNAPSHOT",
  scalaVersion := "2.11.7",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Yno-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture"
  )
)

lazy val core = (project in file("core"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.4.0",
      "com.typesafe.akka" %% "akka-testkit" % "2.4.0" % "test",
      "com.github.nscala-time" %% "nscala-time" % "2.2.0",
      "org.scalatest" %% "scalatest" % "2.2.5" % "test"
    )
  )

lazy val example = (project in file("example"))
  .settings(commonSettings: _*)
  .settings(Revolver.settings: _*)
  .dependsOn(core)