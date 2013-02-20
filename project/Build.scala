import sbt._
import Keys._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization  := "com.github.mvollebregt",
    version       := "0.1",
    scalaVersion  := "2.10.0",
    scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-Xlog-free-terms")
  )
}

object ScalaMacroDebugBuild extends Build {
  import BuildSettings._

  lazy val root: Project = Project(
    "root",
    file("."),
    settings = buildSettings
  ) aggregate(macros, examples)

  lazy val macros: Project = Project(
    "macros",
    file("macros"),
    settings = buildSettings ++ Seq(
      libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-compiler" % _))
  )

  lazy val examples: Project = Project(
    "examples",
    file("examples"),
    settings = buildSettings ++ Seq(
      libraryDependencies += "org.specs2" %% "specs2" % "1.13" % "test"
    )
  ) dependsOn(macros)
}
