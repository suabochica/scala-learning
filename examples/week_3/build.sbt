scalaVersion := "3.0.1"

libraryDependencies ++= List(
    "com.lihaoyi" %% "fansi" % "0.2.14",
    "org.scalameta" %% "munit" % "0.7.22" % Test,
) 

testFrameworks += TestFramework("munit.Framework")
makeSite / mappings := {
    val indexFile = target.value / "index.html"
    IO.write(indexFile, "<h1>Hello, sbt</h1>")
    Seq(indexFile -> "index.html")
}