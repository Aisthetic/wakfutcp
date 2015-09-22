
lazy val wakfuTcp = ProjectRef(uri("git://github.com/jac3km4/wakfutcp.git"), "wakfutcp")

lazy val root = (project in file("."))
  .settings(
    version := "0.11",
    scalaVersion := "2.11.7"
  ).dependsOn(wakfuTcp)