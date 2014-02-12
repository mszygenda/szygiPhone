name := "phone-server"

version := "1.0"

scalaVersion := "2.11.0-M4"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11.0-M4" % "2.2.0"

libraryDependencies += "org.facebook4j" % "facebook4j-core" % "2.0.4"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += "spray" at "http://repo.spray.io/"

resolvers += "akka" at "http://repo.typesafe.com/typesafe/snapshots/"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "akka2" at "http://repo.typesafe.com/typewsafe/releases/"

resolvers += "akka3" at "http://repo.akka.io/releases/"