package io.gourd.flink.scala.games.batch.transformations

import org.apache.flink.api.java.aggregation.Aggregations
import org.apache.flink.api.scala._

/**
  * @author Li.Wei by 2019/11/13
  */
object AggregateOnGroupedTupleDataSet extends Transformations {
  roleLoginDs
    .map(o => (o.uid, o.dataUnix, o.money))
    .filter(_._1.contains("0|102")) // 筛选部分用户
    .groupBy(0)
    .aggregate(Aggregations.SUM, 2)
    // .aggregate(Aggregations.MIN, 1)
    // .andMin(1)
    .print()
  /*
  .aggregate(Aggregations.SUM, 2) 计算结果为
  (0|1021,1571473389,396.0)
  (0|1024,1571543169,50.0)
  (0|1025,1571420259,648.0)
  (0|1025_199,1571577819,648.0)
  */
  /*
  .aggregate(Aggregations.SUM, 2).andMin(1) 计算结果为
  (0|1021,1571471529,396.0)
  (0|1024,1571543169,50.0)
  (0|1025,1571420259,648.0)
  (0|1025_199,1571577819,648.0)
   */
  /*
  .aggregate(Aggregations.SUM, 2).aggregate(Aggregations.MIN, 1) 计算结果为
  (0|1025_199,1571420259,648.0)
   */
}
