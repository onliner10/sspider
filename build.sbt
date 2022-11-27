ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.github.onliner10"
ThisBuild / name := "sspider"

ThisBuild / scalaVersion := "3.2.1"

ThisBuild / scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _)) => Seq("-Ykind-projector:underscores")
    case Some((2, 12 | 13)) => Seq("-Xsource:3", "-P:kind-projector:underscore-placeholders")
  }
}

// https://mvnrepository.com/artifact/org.typelevel/cats-free
libraryDependencies += "org.typelevel" %% "cats-free" % "2.9.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.4.1"

lazy val root = (project in file("."))
  .settings(
    name := "sspider"
  )
