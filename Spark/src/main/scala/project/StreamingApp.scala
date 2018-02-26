package project

import java.sql.DriverManager

import dao.CourseClickDAO
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.sql.types._
import project.domain.{ClickLog, CourseClickCount}

import scala.collection.mutable.ListBuffer

/**
  * Created by Administrator on 2018/2/2 0002.
  */
object StreamingApp {
  def main(args: Array[String]): Unit = {

    val sparkConf=new SparkConf().setAppName("StreamingApp").setMaster("local[2]")
    //流式处理与SQL处理对于的格式不同
    val ssc=new StreamingContext(sparkConf,Seconds(60))
    //通用入口
    val scSQL=new SparkContext(sparkConf)
    //Spark SQL运行入口
    val sparksession = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()


    if(args.length!=4){
      println("Usage:need right param")
      System.exit(1)
    }

    val Array(zk,groupId,topic,thread)=args

    val topicMap=topic.split(",").map((_,thread.toInt)).toMap

    //创建Receiver
    //多个zookeeper的地址，consumer的Id，TopicMap，它的名字和分区数每个分区能被单独的线程消耗,因为topic依赖于zookeeper创建故可以直接发现
    val messages =KafkaUtils.createStream(ssc,zk,groupId,topicMap)

    val logs=messages.map(_._2)


    val cleanData=logs.map(line=>{
      val v1=line.split("\t")
      println(v1(0))
      val date="1997"
      //val date=DateUtil.parseTime(v1(1))
      val classNum=v1(2).split(" ")(2)
      if(classNum.startsWith("/class")){
        val html=classNum.split("/")(2)
        val num=html.substring(0,html.lastIndexOf("."))
        ClickLog(v1(0),date,num.toInt)
      }else{
        ClickLog("0","11",0)
      }
    }).filter(course=>course.ip!="0")


    cleanData.map(x=>{
      (x.date+" "+x.classnum,1)
    }).reduceByKey(_+_).foreachRDD(rdd=>{
      rdd.foreachPartition(partionRecords=>{
        val list=new ListBuffer[CourseClickCount]
        partionRecords.foreach(pair=>{
          list.append(CourseClickCount(pair._1,pair._2))
        })
       CourseClickDAO.save(list)
      })
    })

    /*
    插入MySQL模型，一批数据创建一个connection,进一步优化可添加一个连接池
    但是大数据不常用NoSQL,Hbase,Redis用的比较多
     */
    messages.foreachRDD{rdd=>
      rdd.foreachPartition{partitionOfRecords=>
          val connection=createConnection()
          partitionOfRecords.foreach(record=>{
            val sql=record._1+""+record._2
            connection.createStatement().execute(sql)
          })
          connection.close()
      }
    }

    println(cleanData)
    //HBase的创建语法


    /*
    文本数据转化成RDD
     */
    //文本数据都是这个格式hdfs://  s3a://
    val log=scSQL.textFile("file://")
    log.count()
    //将RDD转化成DataFRame
    val schemaString="age sex"
    val logRDD=log.map(x=>Row(x))
    val fields=schemaString.split(" ").map(filedName=>StructField(filedName,StringType,nullable = true))
    val schema=StructType(fields)
    val logDF=sparksession.createDataFrame(logRDD,schema)
    //若用JSON Parquet它可以自己推测出来不用指定模式
    val loFD=sparksession.read.format("parquet").load("path")
    //HDFS S3
    val loFD2=sparksession.read.format("parquet").load("hdfs://;s3a://")
    //转化后注册成表并进行SQL操作
    logDF.createTempView("logger")
    val sqlDF=sparksession.sql("select * from logger")
    sqlDF.show()


  }
  def createConnection()={
    Class.forName("com.mysql.jdbc.driver")
    DriverManager.getConnection("url name password")
  }



}
