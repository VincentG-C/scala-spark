name := "csv-reader"

version := "0.1"

scalaVersion := "2.12.13"

lazy val commonDependencies = Seq(
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.1.1",
  "org.apache.spark" %% "spark-core" % "3.1.1",
  "org.apache.spark" %% "spark-sql" % "3.1.1"
)

lazy val producer = project.settings(Seq(target := { baseDirectory.value / "producer" }), libraryDependencies ++= commonDependencies)

lazy val consumer = project.settings(Seq(target := { baseDirectory.value / "consumer" }), libraryDependencies ++= commonDependencies)
