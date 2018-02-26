package project.Util

import java.util.Date

import org.apache.commons.lang3.time.FastDateFormat




/**
  * Created by Administrator on 2018/2/2 0002.
  */
object DateUtil {
  val first=FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")
  val after=FastDateFormat.getInstance("yyyyMMddHHmmss")
  def getTime(time:String)={
    first.parse(time).getTime
  }

  def parseTime(time:String)={
    after.format(new Date(getTime(time)))
  }

  def main(args: Array[String]): Unit = {
    println(parseTime("1997-10-17 00:00:00"))
  }




}
