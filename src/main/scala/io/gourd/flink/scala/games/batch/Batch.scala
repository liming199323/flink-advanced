package io.gourd.flink.scala.games.batch

import io.gourd.flink.scala.MainApp
import io.gourd.flink.scala.games.Games._
import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.common.operators.base.JoinOperatorBase.JoinHint
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.table.api.scala.{BatchTableEnvironment, _}

/** Batch 任务 API 使用示例
  * [[BatchDataSet]]
  * [[BatchTable]]
  * [[BatchSQL]]
  *
  * @author Li.Wei by 2019/10/30
  */
trait Batch extends MainApp {

  val env = ExecutionEnvironment.getExecutionEnvironment

  // 用户登录数据 DataSet
  val userLoginDataSet = dataSetFromUserLogin(env)
  // 角色登录数据 DataSet
  val roleLoginDataSet = dataSetFromRoleLogin(env)

}

object BatchDataSet extends Batch {

  userLoginDataSet
    .filter(_.dataUnix > 1571414499)
    .filter(_.status == "LOGIN")
    .join(roleLoginDataSet, JoinHint.BROADCAST_HASH_FIRST).where(_.uid).equalTo(_.uid)
    .apply((left, _) => left.platform -> 1)
    .groupBy(0)
    .sum(1)
    .sortPartition(1, Order.ASCENDING)
    .print()
}

object BatchTable extends Batch {
  val tEnv = BatchTableEnvironment.create(env)
  // 用户登录数据 Table
  val userLoginTable = tEnv.fromDataSet[UserLogin](userLoginDataSet)
  // 角色登录数据 Table
  val roleLoginTable = tEnv.fromDataSet[RoleLogin](roleLoginDataSet)

  userLoginTable
    .select("platform,dataUnix,uid,status")
    .where('dataUnix > 1571414499 && 'status === "LOGIN")
    .join(roleLoginTable.select("uid as r_uid"), "uid = r_uid")
    .groupBy("platform")
    .select("platform as p , count(platform) as c")
    .orderBy('c.asc)
    .toDataSet[(String, Long)]
    .print()
}

object BatchSQL extends Batch {
  val tEnv = BatchTableEnvironment.create(env)
  // 用户登录数据 Table
  val userLoginTable = tEnv.fromDataSet[UserLogin](userLoginDataSet)
  // 角色登录数据 Table
  val roleLoginTable = tEnv.fromDataSet[RoleLogin](roleLoginDataSet)

  tEnv.registerDataSet("userLoginTable", userLoginTable)
  tEnv.registerDataSet("roleLoginTable", roleLoginTable)
  tEnv.sqlQuery(
    """
      |SELECT platform AS p,COUNT(platform) AS c FROM
      |(
      |SELECT platform,dataUnix,uid,status FROM userLoginTable
      |WHERE dataUnix > 0 AND status = 'LOGIN'
      |)
      |GROUP BY platform
      |""".stripMargin)
    .toDataSet[(String, Long)]
    .print()
}