name := """rest-api-play"""
organization := "com.ventura"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.10"

libraryDependencies += guice
libraryDependencies ++= Seq(
  jdbc,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  "org.postgresql" % "postgresql" % "42.2.5"

)

libraryDependencies += "org.playframework.anorm" %% "anorm" % "2.7.0"


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.ventura.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.ventura.binders._"
