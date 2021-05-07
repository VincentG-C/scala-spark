name := "csv-reader"

version := "0.1"

scalaVersion := "2.12.13"

val commonResolvers = Seq(
  "DataStax Repo" at "https://repo.datastax.com/public-repos/"
)

lazy val commonDependencies = Seq(
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.1.1",
  "org.apache.spark" %% "spark-core" % "3.1.1",
  "org.apache.spark" %% "spark-sql" % "3.1.1"
)

lazy val consumerDependencies = Seq(
  "org.codehaus.jackson" % "jackson-core-asl" % "1.9.13",
  "com.datastax.spark" %% "spark-cassandra-connector" % "3.0.1",
  "com.github.jnr" % "jnr-posix" % "3.1.5",
  "com.google.guava" % "guava" % "30.1.1-jre",
"com.datastax.dse" % "dse-spark-dependencies" % "provided" % "6.8.1" exclude ("org.codehaus.jackson", "jackson-core-asl")
)

lazy val producer = project.settings(Seq(target := { baseDirectory.value / "producer" }), libraryDependencies ++= commonDependencies)

lazy val consumer = project.settings(Seq(target := { baseDirectory.value / "consumer" }), libraryDependencies ++= commonDependencies ++ consumerDependencies, resolvers ++= commonResolvers, excludeDependencies ++= Seq(
  // commons-logging is replaced by jcl-over-slf4j
  ExclusionRule("jackson-core-asl", "jackson-core-asl")
))