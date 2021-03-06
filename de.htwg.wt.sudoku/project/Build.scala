import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "SudokuPlay"
    val appVersion      = "1.0"

    val appDependencies = Seq(
        
      "de.htwg.sa" %% "sudoku" % "0.0.1-SNAPSHOT"
      
    )

    val main = PlayProject(appName, appVersion, appDependencies).settings(defaultScalaSettings:_*).settings(
      
      resolvers += "HTWG Resolver" at "http://lenny2.in.htwg-konstanz.de:8081/artifactory/libs-snapshot-local"
      
      publishTo := Some("HTWG Publisher" at "http://lenny2.in.htwg-konstanz.de:8081/artifactory/libs-snapshot-local;build.timestamp=" + new java.util.Date().getTime) ) 
            
    )

}