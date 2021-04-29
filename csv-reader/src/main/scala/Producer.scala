import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

object ReadCSVFile {

  case class Employee(key: String, value: String, designation: String, manager: String, hire_date: String, sal: String, deptno: String)

  def main(args: Array[String]): Unit = {
    var conf = new SparkConf().setAppName("Read CSV File").setMaster("local[*]")
    val sc = new SparkContext(conf)


    val spark = SparkSession
      .builder
      .appName("StructuredNetworkWordCount")
      .getOrCreate()

    val schema = ScalaReflection.schemaFor[Employee].dataType.asInstanceOf[StructType]
    val csvDF = spark.readStream.option("sep", ",").schema(schema).csv("src/main/resources")
    val csvDS = csvDF.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "127.0.0.1:25565,localhost:25566")
      .option("checkpointLocation", "src/main/resources") // <-- checkpoint directory
      .option("topic", "topic1")
      .start()

    while (true) {

    }
    csvDS.stop()
    sc.stop()
  }
}