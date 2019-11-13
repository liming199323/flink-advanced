package io.gourd.flink.scala.games.batch.transformations
import org.apache.flink.api.scala._
/**
  * @author Li.Wei by 2019/11/13
  */
object MaxMinByOnGroupedTupleDataSet extends Transformations {
  roleLoginDs
    .map(o => (o.uid, o.dataUnix, o.money))
    .filter(_._1.contains("1|1051"))
    .groupBy(0)
    .minBy(1, 2)
    .print()
  /* 原始数据集
  (1|1051,1571798859,648.0)
  (1|1051,1571798859,328.0)
  (1|1051,1571798860,198.0)
  (1|1051,1571798861,64.0)
  (1|1051,1571798861,50.0)
   */

  /* 计算结果为
  (1|1051,1571798859,328.0)
   */
}
