/**
  * Created by Administrator on 2018/1/31 0031.
  */
object StartStreamingApp {
  def main(args: Array[String]): Unit = {
    //两种初始化List的方式
    val fruit=1::2::3::Nil
    val animal=List("tiger","dog","pig")
    //两个List可以相连
    val fa=fruit++animal
    //依次为访问List头，尾元素，List是否为空，查找List中指定元素的位置，根据索引(以0开始)访问List
    fa.head
    fa.tail
    fa.isEmpty
    fa.indexOf(0)
    fa.apply(4)
    //根据特定条件过滤掉部分元素，注意fa并没有改变，此时产生一个新的List
    val fa1=fa.filter(x=>x!=1)
    //将List每个元素映射成另一个元素
    val fa2=fa.map(x=>x+"1")
    //flatMap将嵌套List变成一层List
    val fa3=List(1,2,3)
    //List(List(0, 1, 2), List(1, 2, 3), List(2, 3, 4))
    val fa4=fa3.map(x=>g(x))
    //List(0, 1, 2, 1, 2, 3, 2, 3, 4)
    val fa5=fa3.flatMap(x=>g(x))
    //向左规约，即从最左边其将第一第二个元素实行操作，并将操作结果
    //当成下次操作的参数之一与三个元素实现操作，直到最右边，结果是18
    fa5.reduceLeft((x,y)=>x+y)
    //4表示初始值，y表示List中的下一个元素，x表示上次的结果，结果是10
    val fa6=fa3.foldLeft(4)((x,y)=>x+y)

    //初始化语法
    val things=("1",2,200.0)
    //取得tuple的第一个元素
    things._1
    //证明它是一个类，结果是class scala.Tuple3
    things.getClass
    //使用模式匹配为things元素命名，string对应的值就是"1"
    val (string,number,float)=things
    //遍历输出tuple中的每一个元素
    //things.productIterator.foreach(println)

    //初始语法
    val map=Map(1->"J",2->"Z",3->"B")
    //指定key访问元素
    println(map(1))
    //判断Key是否在元素里面
    println(map.contains(4))
    //Map中添加元素
    map+(4->"Z")
    //Map中删除元素
    map-3
    //添加多个元素
    val map2=map++List(5->"W",6->"C")
    //删除多个元素
    val map1=map--List(1,2,3)

    Traversable(map)




  }
  def g(v:Int)=List(v-1,v,v+1)


}
