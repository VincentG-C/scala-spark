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

    val schema = ScalaReflection.schemaFor[Employee].dataType.asInstanceOf[StructType]


    val spark = SparkSession
      .builder
      .appName("KafkaReader")
      .getOrCreate()

    import spark.implicits._

    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "topic1")
      .load()
    df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .as[(String, String)]




    df.printSchema()
    //val flattenDF = df.select($"*")
    //flattenDF.show()

    sc.stop()

  }

}