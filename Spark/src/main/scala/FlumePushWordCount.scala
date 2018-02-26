import org.apache.spark.SparkConf
import org.apache.spark.streaming.flume.FlumeUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by Administrator on 2018/1/29 0029.
  */
object FlumePushWordCount {
  def main(args: Array[String]): Unit = {
    val saprkConf =new SparkConf().setMaster("local[2]").setAppName("Flume_Count")
    val ssc=new StreamingContext(saprkConf,Seconds(5))

    val flumeStream=FlumeUtils.createStream(ssc,"localhost",41414)

    flumeStream.map(x=>new String(x.event.getBody.array()))
      .flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_).print()

    ssc.start()
    ssc.awaitTermination()


  }



}
