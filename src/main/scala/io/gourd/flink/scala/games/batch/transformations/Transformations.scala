package io.gourd.flink.scala.games.batch.transformations

import io.gourd.flink.scala.api.BatchExecutionEnvironmentApp
import io.gourd.flink.scala.games.data.GameData.DataSet

/**
  * @author Li.Wei by 2019/11/13
  */
trait Transformations extends BatchExecutionEnvironmentApp {

  // 用户登录数据 DataSet
  val userLoginDs = DataSet.userLogin(this)

  // 角色付费数据 DataSet
  val roleLoginDs = DataSet.rolePay(this)

}
