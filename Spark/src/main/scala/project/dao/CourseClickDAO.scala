package project.dao

import etalier.spark.utils.HBaseUtils
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes
import project.domain.CourseClickCount

import scala.collection.mutable.ListBuffer

/**
  * Created by Administrator on 2018/2/3 0003.
  */
object CourseClickDAO {

  val table_name="course_clickcount"
  val cf="info"
  val qualifer="click_count"

  def save(list:ListBuffer[CourseClickCount])={
    val table= HBaseUtils.getInstance().getTable(table_name)

    for(ele<-list){
      //把rowKey对应的数量自动加上来
      table.incrementColumnValue(Bytes.toBytes(ele.day_course),
        Bytes.toBytes(cf),
        Bytes.toBytes(qualifer),
        ele.click_count)
    }
  }

  def count(day_course:String):Long={
    val table= HBaseUtils.getInstance().getTable(table_name)

    val get=new Get(Bytes.toBytes(day_course))
    val value=table.get(get).getValue(cf.getBytes(),qualifer.getBytes())

    if(value==null){
      0
    }else{
      Bytes.toLong(value)
    }
  }





}
