ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.github.onliner10"
ThisBuild / scalaVersion := "3.2.1"

ThisBuild / scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _)) => Seq("-Ykind-projector:underscores")
    case Some((2, 12 | 13)) =>
      Seq("-Xsource:3", "-P:kind-projector:underscore-placeholders")
  }
}

lazy val dependencies =
  new {
    val catsFreeV = "2.9.0"
    val catsEffectV = "3.4.1"
    val scalaUriV = "4.0.3"

    val catsFree = "org.typelevel" %% "cats-free" % catsFreeV
    val catsEffect = "org.typelevel" %% "cats-effect" % catsEffectV
    val scalaUri = "io.lemonlabs" %% "scala-uri" % scalaUriV
  }

lazy val testDependencies =
  new {
    val scalaCheckV = "1.15.4"
    val scalaTestPlusV = "3.2.14.0"
    val scalaticV = "3.2.14"
    val scalaTestV = "3.2.14"

    val scalatic = "org.scalactic" %% "scalactic" % "3.2.14" % "test"
    val scalaTest = "org.scalatest" %% "scalatest" % scalaTestV % "test"
    val scalaCheck = "org.scalacheck" %% "scalacheck" % scalaCheckV % "test"
    val scalaTestPlus = "org.scalatestplus" %% "scalacheck-1-17" % scalaTestPlusV % "test"
  }

lazy val commonDependencies = Seq(
  dependencies.catsFree,
  dependencies.catsEffect
)

lazy val commonTestDependencies = Seq(
  testDependencies.scalaCheck,
  testDependencies.scalaTestPlus,
  testDependencies.scalatic,
  testDependencies.scalaTest
)

lazy val settings =
  Seq()

lazy val root = (project in file("."))
  .aggregate(core, csvSink, example)

lazy val core = (project in file("core"))
  .settings(
    settings,
    name := "sspider-core",
    libraryDependencies ++= commonDependencies ++ commonTestDependencies ++ Seq(
      dependencies.scalaUri
    )
  )

lazy val csvSink = (project in file("sinks/csv"))
  .settings(
    name := "sspider-csv-sink",
    settings,
    libraryDependencies ++= commonDependencies ++ commonTestDependencies
  )
  .dependsOn(core)

lazy val example = (project in file("example"))
  .settings(
    settings,
    name := "sspider-example",
    Compile / run / mainClass := Some(
      "com.github.onliner10.sspider.example.ExampleApp"
    )
  )
  .dependsOn(core)
  .dependsOn(csvSink)
