package io.gourd.flink.scala.games.batch

import io.gourd.flink.scala.api.BatchExecutionEnvironmentApp
import io.gourd.flink.scala.games.data.GameData.DataSet
import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.java.aggregation.Aggregations
import org.apache.flink.api.scala._

/**
  * 函数扩展，支持偏函数应用
  * https://ci.apache.org/projects/flink/flink-docs-release-1.9/dev/scala_api_extensions.html#dataset-api
  *
  * @example
  * {{{
  *
  * data.mapWith {
  * case (_, value) => value.toString
  * }
  *
  * data.mapPartitionWith {
  * case head #:: _ => head
  * }
  *
  * data.flatMapWith {
  * case (_, name, visitTimes) => visitTimes.map(name -> _)
  * }
  *
  * data.filterWith {
  * case Train(_, isOnTime) => isOnTime
  * }
  *
  * data.reduceWith {
  * case ((_, amount1), (_, amount2)) => amount1 + amount2
  * }
  *
  * data.reduceGroupWith {
  * case id #:: value #:: _ => id -> value
  * }
  *
  * data.groupingBy {
  * case (id, _, _) => id
  * }
  * grouped.sortGroupWith(Order.ASCENDING) {
  * case House(_, value) => value
  * }
  * grouped.combineGroupWith {
  * case header #:: amounts => amounts.sum
  * }
  *
  * data1.join(data2).
  * whereClause(case (pk, _) => pk).
  * isEqualTo(case (_, fk) => fk).
  * projecting {
  * case ((pk, tx), (products, fk)) => tx -> products
  * }
  *
  * data1.cross(data2).projecting {
  * case ((a, _), (_, b) => a -> b
  * }
  *
  * data1.coGroup(data2).
  * whereClause(case (pk, _) => pk).
  * isEqualTo(case (_, fk) => fk).
  * projecting {
  * case (head1 #:: _, head2 #:: _) => head1 -> head2
  * }
  * }
  * }}}
  * @author Li.Wei by 2019/11/4
  */
object DataSetExtensions extends BatchExecutionEnvironmentApp {

  // 用户登录数据 DataSet
  val rolePayDataSet = DataSet.rolePay(this)

  import org.apache.flink.api.scala.extensions._ // 引入 API 以支持 funWith

  rolePayDataSet
    .map(o => (o.dataUnix, o.rid, o.money))
    .filterWith {
      case (_, _, money) => money > 50
    }
    .mapWith {
      case (_, rid, money) => (rid, money)
    }
    .groupBy(0)
    .sortGroup(1, Order.ASCENDING)
    .aggregate(Aggregations.SUM, 1)
    .print()

}
