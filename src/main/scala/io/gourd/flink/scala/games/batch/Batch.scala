package io.gourd.flink.scala.games.batch

import io.gourd.flink.scala.games.data.GameData
import io.gourd.flink.scala.{BatchLocalApp, BatchTableLocalApp}
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

object BatchDataSet extends BatchLocalApp {

  // 用户登录数据 DataSet
  val userLoginDataSet = GameData.loadUserLoginDs(this)
  // 角色登录数据 DataSet
  val roleLoginDataSet = GameData.loadRoleLoginDs(this)

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

object BatchTable extends BatchTableLocalApp {

  private val userLogin = GameData.registerUserLoginDs(this)
  private val roleLogin = GameData.registerRoleLoginDs(this)

  tEnv.scan(userLogin)
    .select("platform,dataUnix,uid,status")
    .where('dataUnix > 1571414499 && 'status === "LOGIN")
    .join(tEnv.scan(roleLogin).select("uid as r_uid"), "uid = r_uid")
    .groupBy("platform")
    .select("platform as p , count(platform) as c")
    .orderBy('c.asc)
    .toDataSet[(String, Long)]
    .print()
}

object BatchSQL extends BatchTableLocalApp {

  private val table = GameData.registerUserLoginDs(this)

  tEnv.sqlQuery(
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