val scala3Version = "3.9.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Crownfall",
    version := "v0.3.0",

    scalaVersion := scala3Version,

    Test / parallelExecution := false,

    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.20",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.20" % "test",
    libraryDependencies += "com.tngtech.archunit" % "archunit" % "1.5.1" % Test,

    // Einstiegspunkt des Spiels
    Compile / mainClass := Some("crownfall.hello"),

    // Name der ausführbaren JAR-Datei
    assembly / assemblyJarName := "crownfall.jar",

    // Einstiegspunkt für die JAR-Datei
    assembly / mainClass := Some("crownfall.hello")
  )