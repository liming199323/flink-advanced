package io.gourd.flink.scala.games.batch

import io.gourd.flink.scala.api.{BatchExecutionEnvironmentApp, BatchTableEnvironmentApp}
import io.gourd.flink.scala.games.data.GameData.{DataSet, RegisterDataSet}
import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.common.operators.base.JoinOperatorBase.JoinHint
import org.apache.flink.api.scala._
import org.apache.flink.table.api.scala._

/** Batch 任务 API 使用示例
  * [[BatchDataSet]]
  * [[BatchTable]]
  * [[BatchSQL]]
  *
  * @author Li.Wei by 2019/10/30
  */
trait Batch

// 操作原始 DataSet API 完成 2个表数据过滤join聚合操作
object BatchDataSet extends BatchExecutionEnvironmentApp {

  // 用户登录数据 DataSet
  val userLoginDataSet = DataSet.userLogin(this)
  // 角色登录数据 DataSet
  val roleLoginDataSet = DataSet.roleLogin(this)

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

// 操作 Table API 完成 2个表数据过滤join聚合操作
object BatchTable extends BatchTableEnvironmentApp {

  private val userLogin = RegisterDataSet.userLogin(this)
  private val roleLogin = RegisterDataSet.roleLogin(this)

  btEnv.scan(userLogin)
    .select("platform,dataUnix,uid,status")
    .where('dataUnix > 1571414499 && 'status === "LOGIN")
    .join(btEnv.scan(roleLogin).select("uid as r_uid"), "uid = r_uid")
    .groupBy("platform")
    .select("platform as p , count(platform) as c")
    .orderBy('c.asc)
    .toDataSet[(String, Long)]
    .print()
}

// 操作 SQL 完成 2个表数据过滤join聚合操作
object BatchSQL extends BatchTableEnvironmentApp {

  private val table = RegisterDataSet.userLogin(this)

  btEnv.sqlQuery(
    s"""
       |SELECT platform AS p,COUNT(platform) AS c FROM
       |(
       |SELECT platform,dataUnix,uid,status FROM $table
       |WHERE dataUnix > 0 AND status = 'LOGIN'
       |)
       |GROUP BY platform
       |""".stripMargin)
    .toDataSet[(String, Long)]
    .print()
}