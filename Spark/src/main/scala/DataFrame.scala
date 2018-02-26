import org.apache.spark.sql.SparkSession

/**
  * Created by Administrator on 2018/2/9 0009.
  */
object DataFrame {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("DataFrameApp").master("local[2]").getOrCreate()

    val peopleDF=spark.read.format("json").load("path")

    peopleDF.printSchema()

    //默认显示前20条记录
    peopleDF.show(100)

    peopleDF.select("name").show()

    peopleDF.select(peopleDF.col("name"),(peopleDF.col("age")+10).as("agew")).show()

    peopleDF.filter(peopleDF.col("age")>19).show()

    //相同列值分组并输出个数
    peopleDF.groupBy("age").count().show()

    val rdd=spark.sparkContext.textFile("path")

    import spark.implicits._
    //val df=rdd.map(_.split(",")).map(line=>Info(line(0),line(1).toInt).toDF()


    peopleDF.createTempView("infos")
    //与上面一起构成两种编程方式 sql或DataFrame
    spark.sql("selec *from ....")

      spark.stop()



  }

  case class Info(name:String,age:Int)



}
