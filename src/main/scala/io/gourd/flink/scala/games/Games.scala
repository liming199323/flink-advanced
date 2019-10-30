package io.gourd.flink.scala.games

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}

/** 使用 resource/data/game/ 作为数据源
  *
  * @author Li.Wei by 2019/10/29
  */
object Games {

  /**
    * 模型定义
    */
  trait GameModel {

    def dataUnix: Int
  }

  // 用户登录
  case class UserLogin(platform: String, channel: String, region: String, server: String,
                       uid: String,
                       dataUnix: Int,
                       status: String) extends GameModel

  // 角色登录
  case class RoleLogin(platform: String, channel: String, region: String, server: String,
                       uid: String,
                       rid: String,
                       dataUnix: Int,
                       status: String) extends GameModel

  // 角色充值
  case class RolePay(platform: String, channel: String, region: String, server: String,
                     uid: String,
                     rid: String,
                     dataUnix: Int,
                     payWay: String, orderId: String, valid: String, money: Double) extends GameModel

  // 角色商城消费
  case class RoleShop(platform: String, channel: String, region: String, server: String,
                      uid: String,
                      rid: String,
                      dataUnix: Int,
                      mallId: String, itemId: String, currencyType: String,
                      num: Long, singleMoney: Double, totalMoney: Double) extends GameModel

  def dataSetFromUserLogin(env: ExecutionEnvironment): DataSet[UserLogin] =
    env.readCsvFile[UserLogin](
      getClass.getClassLoader.getResource("data/game/user-login.csv").getPath,
      fieldDelimiter = ",",
      ignoreFirstLine = true
    )

  def dataSetFromRoleLogin(env: ExecutionEnvironment): DataSet[RoleLogin] =
    env.readCsvFile[RoleLogin](
      getClass.getClassLoader.getResource("data/game/role-login.csv").getPath,
      fieldDelimiter = ",",
      ignoreFirstLine = true
    )

  def dataSetFromRolePay(env: ExecutionEnvironment): DataSet[RolePay] =
    env.readCsvFile[RolePay](
      getClass.getClassLoader.getResource("data/game/role-pay.csv").getPath,
      fieldDelimiter = ",",
      ignoreFirstLine = true
    )

  def dataSetFromRoleShop(env: ExecutionEnvironment): DataSet[RoleShop] =
    env.readCsvFile[RoleShop](
      getClass.getClassLoader.getResource("data/game/role-shop.csv").getPath,
      fieldDelimiter = ",",
      ignoreFirstLine = true
    )
}
