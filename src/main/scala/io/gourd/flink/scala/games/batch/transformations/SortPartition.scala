package io.gourd.flink.scala.games.batch.transformations

import java.lang

import io.gourd.flink.scala.games.data.pojo.RolePay
import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala._
import org.apache.flink.util.Collector

import scala.collection.JavaConverters._

/**
  * @author Li.Wei by 2019/11/13
  */
object SortPartition extends Transformations {

  roleLoginDs
    .partitionByHash(_.dataUnix)
    .mapPartition((values: lang.Iterable[RolePay], out: Collector[(String, Int, Double)]) => {
      values.iterator().asScala.foreach(o => out.collect(o.rid, o.dataUnix, o.money))
    })
    .print()


  roleLoginDs
    .partitionByRange(_.dataUnix)
    .mapPartition((values: lang.Iterable[RolePay], out: Collector[(String, Int, Double)]) => {
      values.iterator().asScala.foreach(o => out.collect(o.rid, o.dataUnix, o.money))
    })
    .print()


  roleLoginDs
    .sortPartition(_.dataUnix, Order.ASCENDING)
    .sortPartition(_.uid, Order.DESCENDING)
    .mapPartition((values: lang.Iterable[RolePay], out: Collector[(String, Int, Double)]) => {
      values.iterator().asScala.foreach(o => out.collect(o.rid, o.dataUnix, o.money))
    })
    .rebalance()
    .print()

}
