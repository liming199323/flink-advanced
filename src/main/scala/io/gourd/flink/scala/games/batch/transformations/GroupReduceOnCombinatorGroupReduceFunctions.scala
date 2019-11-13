package io.gourd.flink.scala.games.batch.transformations

import java.lang

import org.apache.flink.api.common.functions.{GroupCombineFunction, GroupReduceFunction}
import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala._
import org.apache.flink.util.Collector

/** 可组合的GroupReduce函数
  *
  * @author Li.Wei by 2019/11/13
  */
object GroupReduceOnCombinatorGroupReduceFunctions extends Transformations {
  roleLoginDs
    .map(o => (o.uid, o.dataUnix, o.money))
    .filter(_._1.contains("1|1051")) // 筛选部分用户
    .groupBy(0, 1) // 按用户 ID 分组
    .sortGroup(2, Order.ASCENDING) // 分组排序，按订单金额升序
    .reduceGroup(new MyCombinableGroupReducer())
    .distinct()
    .print()

  /* sortGroup 后原始数据集
  (1|1051,1571798861,50.0)
  (1|1051,1571798861,64.0)
  (1|1051,1571798860,198.0)
  (1|1051,1571798859,328.0)
  (1|1051,1571798859,648.0)
 */

  /* 计算结果为
  (1|1051,1571798859,976.0)
  (1|1051,1571798860,198.0)
  (1|1051,1571798861,114.0)
   */
}

/**
  * 与 reduce 函数相比，group-reduce 函数不是可隐式组合的。
  * 为了使 group-reduce 函数可组合，它必须实现 GroupCombineFunction 接口。
  *
  * 要点：GroupCombineFunction 接口的通用输入和输出类型必须等于 GroupReduceFunction 的通用输入类型，
  * 如以下示例所示：
  */

import scala.collection.JavaConverters._

class MyCombinableGroupReducer
  extends GroupReduceFunction[(String, Int, Double), (String, Int, Double)]
    with GroupCombineFunction[(String, Int, Double), (String, Int, Double)] {
  override def reduce(values: lang.Iterable[(String, Int, Double)],
                      out: Collector[(String, Int, Double)]): Unit = {
    values.iterator().asScala.foreach(o => out.collect(o))
  }

  override def combine(values: lang.Iterable[(String, Int, Double)],
                       out: Collector[(String, Int, Double)]): Unit = {
    val r = values.iterator().asScala.reduce((o1, o2) => (o1._1, o1._2, o1._3 + o2._3))
    out.collect(r) // 合并相同 key 的价格
  }
}