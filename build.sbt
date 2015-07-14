name := "wakfutcp"

version := "0.1"

scalaVersion := "2.11.7"

organization := "com.github.jac3km4"

publishMavenStyle := true

scalacOptions ++= Seq("-feature", "-deprecation")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.github.nscala-time" %% "nscala-time" % "2.0.0"
)