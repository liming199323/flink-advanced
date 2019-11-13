package io.gourd.flink.scala.games.batch.transformations

import org.apache.flink.api.scala._
/**
  * @author Li.Wei by 2019/11/13
  */
object CrossWithSizeHint extends Transformations {

  // crossWithTiny => 告诉系统假定右侧比左侧小很多
  roleLoginDs
    .filter(_.uid.equals("0|107"))
    .crossWithTiny(userLoginDs.filter(_.uid.equals("0|107"))) {
      (c1, c2) => (c1.uid, c2.uid)
    }
    .print()

  // crossWithHuge => 告诉系统假定左侧比右侧小很多
  roleLoginDs
    .filter(_.uid.equals("0|107"))
    .crossWithHuge(userLoginDs.filter(_.uid.equals("0|107"))) {
      (c1, c2) => (c1.uid, c2.uid)
    }
    .print()

}
