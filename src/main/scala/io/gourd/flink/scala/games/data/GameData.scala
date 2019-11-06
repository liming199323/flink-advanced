package io.gourd.flink.scala.games.data

import io.gourd.flink.scala.api.{BatchTableEnvironmentApp, ExecutionEnvironmentApp}
import io.gourd.flink.scala.games.data.pojo.{RoleLogin, RolePay, RoleShop, UserLogin}
import org.apache.flink.api.scala.{DataSet, _}
import org.apache.flink.table.expressions.Expression

/** 使用 resource/data/game/ 作为数据源
  *
  * @author Li.Wei by 2019/10/29
  */
object GameData {

  private def registerDs[T](app: BatchTableEnvironmentApp, tableName: String, ds: DataSet[T],
                            fields: Option[Array[Expression]]): String = {
    fields match {
      case Some(value) => app.tEnv.registerDataSet(tableName, ds, value.toSeq: _*)
      case None => app.tEnv.registerDataSet(tableName, ds)
    }
    tableName
  }

  def loadUserLoginDs(app: ExecutionEnvironmentApp): DataSet[UserLogin] =
    app.env.readCsvFile[UserLogin](
      getClass.getClassLoader.getResource("data/game/UserLogin.csv").getPath,
      fieldDelimiter = ",",
      ignoreFirstLine = true
    )


  def registerUserLoginDs(app: BatchTableEnvironmentApp, fields: Option[Array[Expression]] = None): String =
    registerDs[UserLogin](app, "UserLogin", loadUserLoginDs(app), fields)

  def loadRoleLoginDs(app: ExecutionEnvironmentApp): DataSet[RoleLogin] =
    app.env.readCsvFile[RoleLogin](
      getClass.getClassLoader.getResource("data/game/RoleLogin.csv").getPath,
      fieldDelimiter = ",",
      ignoreFirstLine = true
    )

  def registerRoleLoginDs(app: BatchTableEnvironmentApp, fields: Option[Array[Expression]] = None): String =
    registerDs[RoleLogin](app, "RoleLogin", loadRoleLoginDs(app), fields)


  def loadRolePayDs(app: ExecutionEnvironmentApp): DataSet[RolePay] =
    app.env.readCsvFile[RolePay](
      getClass.getClassLoader.getResource("data/game/RolePay.csv").getPath,
      fieldDelimiter = ",",
      ignoreFirstLine = true
    )

  def registerRolePayDs(app: BatchTableEnvironmentApp, fields: Option[Array[Expression]] = None): String =
    registerDs[RolePay](app, "RolePay", loadRolePayDs(app), fields)


  def loadRoleShopDs(app: ExecutionEnvironmentApp): DataSet[RoleShop] =
    app.env.readCsvFile[RoleShop](
      getClass.getClassLoader.getResource("data/game/RoleShop.csv").getPath,
      fieldDelimiter = ",",
      ignoreFirstLine = true
    )

  def registerRoleShopDs(app: BatchTableEnvironmentApp, fields: Option[Array[Expression]] = None): String =
    registerDs[RoleShop](app, "RoleShop", loadRoleShopDs(app), fields)

}
