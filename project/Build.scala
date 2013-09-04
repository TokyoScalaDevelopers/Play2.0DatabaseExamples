import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "tsddit"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "com.typesafe.slick" % "slick_2.10" % "1.0.1",
    "org.postgresql" % "postgresql" % "9.2-1003-jdbc4",
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )

}
