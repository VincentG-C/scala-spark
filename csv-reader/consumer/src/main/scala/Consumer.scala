import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession


object Consumer {

  case class Employee(key: String, value: String, designation: String, manager: String, hire_date: String, sal: String, deptno: String)

  def main(args: Array[String]): Unit = {

    var conf = new SparkConf().setAppName("Read Kafka Topic").setMaster("local[*]")
    val sc = new SparkContext(conf)
    sc.setCheckpointDir("Flan")
    val schema = ScalaReflection.schemaFor[Employee].dataType.asInstanceOf[StructType]



    val spark = SparkSession
      .builder
      .appName("KafkaReader")
      .getOrCreate()

    import spark.implicits._
    //spark.conf.set(s"spark.sql.catalog.mycatalog", "com.datastax.spark.connector.datasource.CassandraCatalog")
    spark.conf.set(s"spark.sql.catalog.cass100.spark.cassandra.connection.host", "127.0.0.1")

    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "topic1")
      .option("checkpointLocation", "flan")
      .load()
    df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .as[(String, String)]

    val xyz = df.writeStream.format("kafka").option("checkpointLocation", "flan").start()

    df.writeStream.format("org.apache.spark.sql.cassandra")
      .option("keyspace", "vincent")
      .option("table", "utilisateur")
      .option("mode", "append").start()

    xyz.stop()
    df.printSchema()
    //val flattenDF = df.select($"*")
    //flattenDF.show()

    sc.stop()

  }

}