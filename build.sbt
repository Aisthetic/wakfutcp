name := "wakfutcp"

version := "0.11"

scalaVersion := "2.11.7"

organization := "com.github"

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

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.0-RC3",
  "com.github.nscala-time" %% "nscala-time" % "2.2.0"
)