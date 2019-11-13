package io.gourd.flink.scala.games.batch.transformations

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala._
import org.apache.flink.util.Collector

/** 应用于分组DataSet的GroupReduce转换为每个组调用用户定义的group-reduce函数。
  * 这与Reduce之间的区别在于用户定义的函数会立即获得整个组。
  * 在组的所有元素上使用Iterable调用该函数，并且可以返回任意数量的结果元素。
  *
  * @author Li.Wei by 2019/11/13
  */
object GroupReduceOnGroupedDataSet extends Transformations {

  roleLoginDs
    .map(o => (o.uid, o.dataUnix, o.money))
    .filter(_._1.contains("1|1051")) // 筛选部分用户
    .groupBy(0, 1) // 按用户 ID 分组
    .sortGroup(2, Order.ASCENDING) // 分组排序，按订单金额升序
    .reduceGroup((in: Iterator[(String, Int, Double)], out: Collector[(String, Int, Double)]) => {
      in.foreach(out.collect)
    })
    .print()

  /* sortGroup 后原始数据集
  (1|1051,1571798861,50.0)
  (1|1051,1571798861,64.0)
  (1|1051,1571798860,198.0)
  (1|1051,1571798859,328.0)
  (1|1051,1571798859,648.0)
 */
}
