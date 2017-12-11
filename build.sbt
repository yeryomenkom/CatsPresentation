name := "CatsPresentation"

version := "0.1"

scalaVersion := "2.12.4"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

libraryDependencies ++= Seq(
  "com.github.mpilquist" %% "simulacrum" % "0.11.0",
  "org.typelevel" %% "cats-core" % "1.0.0-MF",

  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.typelevel" %% "cats-testkit" % "1.0.0-MF" % "test"
)

scalacOptions += "-Ypartial-unification"
        