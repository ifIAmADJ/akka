name := "akka"

version := "0.1"

scalaVersion := "2.12.4"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.12"
libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.4.12"