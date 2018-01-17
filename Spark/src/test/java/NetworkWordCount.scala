import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by Administrator on 2018/1/17 0017
  * 测试 nc.
  */
object NetworkWordCount {

  def main(args: Array[String]): Unit = {

    //为什么设置成2 需要一个资源去处理处理接收的数据
    val sparkConf=new SparkConf().setMaster("local[2]").setAppName("wordCount");

    val ssc=new StreamingContext(sparkConf,Seconds(5))

    val lines=ssc.socketTextStream("119.29.20.230",99)

    val result=lines.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)

    result.print()

    ssc.start()
    ssc.awaitTermination()

  }


}
