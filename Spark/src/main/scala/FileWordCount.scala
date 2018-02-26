import org.apache.hadoop.conf.Configuration
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by Administrator on 2018/1/17 0017.
  */
object FileWordCount {
  def main(args: Array[String]): Unit = {
    //Configuration
    val sparkConf=new SparkConf().setMaster("local").setAppName("wordCount");

    val ssc=new StreamingContext(sparkConf,Seconds(5))

    val lines=ssc.textFileStream("filedir");

    val result=lines.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)

    result.print()

    ssc.start()
    ssc.awaitTermination()

  }


}
