package io.gourd.flink.scala.games.batch.transformations

import org.apache.flink.api.scala._

/**
  * @author Li.Wei by 2019/11/13
  */
object Cross extends Transformations {

  roleLoginDs
    .filter(_.uid.equals("0|107"))
    .cross(userLoginDs.filter(_.uid.equals("0|107"))) {
      (c1, c2) => (c1.uid, c2.uid)
    }
    .print()
}

