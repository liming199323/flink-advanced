package io.gourd.flink.scala.games.batch.transformations

import io.gourd.flink.scala.games.data.pojo.{RolePay, UserLogin}
import org.apache.flink.api.scala._
import org.apache.flink.util.Collector

/**
  * @author Li.Wei by 2019/11/13
  */
object CoGroup extends Transformations {

  roleLoginDs
    .filter(_.uid.contains("0|10"))
    .coGroup(userLoginDs.filter(_.uid.contains("0|10"))).where(_.uid).equalTo(_.uid)
    .apply((o1: Iterator[RolePay], o2: Iterator[UserLogin], out: Collector[(Int, Int)]) => {
      out.collect(o1.size, o2.size)
    })
    .print()
}
