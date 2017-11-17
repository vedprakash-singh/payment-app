name := """payment-app"""
organization := "com.primoris"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(guice, ws)
libraryDependencies ++= Seq(
  "com.h2database" % "h2" % "1.4.196",
  "com.typesafe.play" %% "play-slick" % "3.0.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
)
