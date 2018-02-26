import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by Administrator on 2018/1/17 0017.
  */
object StatefulWordCount {
  def main(args: Array[String]): Unit = {
    val fruit =1::2::Nil



    val sparkConf=new SparkConf().setMaster("local").setAppName("wordCount");

    val ssc=new StreamingContext(sparkConf,Seconds(5))

    val lines=ssc.textFileStream("filedir");

    val result=lines.flatMap(_.split(" ")).map((_,1))
    //隐方式转化
    val state=result.updateStateByKey(updateFunction _)

    //使用了stateful的算子，必须设置checkpoint
    ssc.checkpoint("")

    result.print()

    ssc.start()
    ssc.awaitTermination()

  }

  def updateFunction(currentValues:Seq[Int],preValues:Option[Int]):Option[Int]={
    val current=currentValues.sum
    val pre=preValues.getOrElse(0)
    Some(current+pre)
  }

}
