package dao

import project.Util.DateUtil
import project.domain.ClickLog

/**
  * Created by Administrator on 2018/1/17 0017.
  */
object CourseClickDAO {

  def main(args: Array[String]): Unit = {
    val colors = Map("red" -> "#FF0000", "azure" -> "#F0FFFF")
    val t1=(2,3)
    val t2=Tuple1(2,3)

    val x=",29,s9,90"
//    println(x.split(",")(0))
//    println(x.split(",")(1))

    val llist=List((1,2),(3,4))

    val logs=List("2907\t1997-10-17 10:20:10 \t 'GET /lass/130.html HTTP1.1'","2907\t1997-10-17 10:20:10 \t 'GET /class/139.html HTTP1.1'")

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

//    cleanData.map(x=>{
//      (x.date+" "+x.classnum,1)
//    }).

    //println(cleanData)


  }



}
