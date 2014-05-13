name := """de.htwg.wt.sudoku"""

version := "1.0-SNAPSHOT"

//Add Repository Path
resolvers += "db4o-repo" at "http://source.db4o.com/maven"

libraryDependencies ++= Seq(
  // Select Play modules
  //jdbc,      // The JDBC connection pool and the play.api.db API
  //anorm,     // Scala RDBMS Library
  //javaJdbc,  // Java database API
  //javaEbean, // Java Ebean plugin
  //javaJpa,   // Java JPA plugin
  //filters,   // A set of built-in filters
  javaCore,  // The core Java API
  // WebJars pull in client-side web libraries
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.webjars" % "bootstrap" % "2.3.1",
  "log4j" % "log4j" % "1.2.17",
  "com.google.inject" % "guice" % "3.0",
  "com.google.inject.extensions" % "guice-multibindings" % "3.0",
  "com.db4o" % "db4o-full-java5" % "8.0-SNAPSHOT"
  //"com.google.inject" % "guice-assistedinject" % "3.0"
  // Add your own project dependencies in the form:
  // "group" % "artifact" % "version"
)

play.Project.playScalaSettings

closureCompilerOptions := Seq("rjs")
