val akkaVersion = "2.5.3"
val playVersion = "2.6.3"
val zookeeperVersion = "3.4.9"
val logbackVersion = "1.2.3"
val typesafeConfigVersion = "1.3.1"

lazy val commonSettings = Seq(
  organization := "de.thmshmm",
  version := "0.0.1",
  scalaVersion := "2.12.3"
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "zookeeper-auditor",
    resolvers ++= Seq(
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
    ),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      "com.typesafe" % "config" % typesafeConfigVersion,
      "com.typesafe.play" %% "play-json" % playVersion,
      "org.apache.zookeeper" % "zookeeper" % zookeeperVersion exclude("log4j", "log4j") exclude("org.slf4j", "slf4j-log4j12"),
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    ),
    assemblyJarName in assembly := "zooditor.jar"
  )
