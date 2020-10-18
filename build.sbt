name := """twisdenwood2"""
organization := "stu.bentley"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test

libraryDependencies ++= Seq(
	"io.netty" % "netty-all" % "4.1.52.Final",
	"org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0",
	"com.adrianhurt" %% "play-bootstrap" % "1.5-P27-B4",
	"com.cloudinary" % "cloudinary-http44" % "1.26.0"
)

// Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
EclipseKeys.preTasks := Seq(compile in Compile, compile in Test)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "stu.bentley.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "stu.bentley.binders._"
