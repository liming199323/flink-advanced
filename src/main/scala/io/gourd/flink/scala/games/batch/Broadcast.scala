package io.gourd.flink.scala.games.batch

import io.gourd.flink.scala.MainApp
import io.gourd.flink.scala.games.Games.{UserLogin, dataSetFromRoleLogin, dataSetFromUserLogin}
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.configuration.Configuration

/** 广播变量
  * https://ci.apache.org/projects/flink/flink-docs-release-1.9/zh/dev/batch/#broadcast-variables
  *
  * @author Li.Wei by 2019/11/4
  */
object Broadcast extends MainApp {
  val env = ExecutionEnvironment.getExecutionEnvironment

  // 用户登录数据 DataSet
  val userLoginDataSet = dataSetFromUserLogin(env)

  // 角色登录数据 DataSet 对应用户ID,去重
  val roleLoginDataSet = dataSetFromRoleLogin(env).map(_.uid).distinct()

  userLoginDataSet
    .map(new RichMapFunction[UserLogin, (String, String)] {
      var broadcastSet: Traversable[String] = _

      override def open(config: Configuration): Unit = {
        // 3. Access the broadcast DataSet as a Collection
        import scala.collection.JavaConverters._
        broadcastSet = getRuntimeContext.getBroadcastVariable[String]("roleLoginDataSet").asScala
      }

      // 判断当前用户对应的ID在该用户对应角色中是否登录过
      override def map(value: UserLogin): (String, String) =
        if (broadcastSet.exists(_ == value.uid)) (value.uid, value.status) else ("none", value.status)

    }).withBroadcastSet(roleLoginDataSet, "roleLoginDataSet")
    .first(10)
    .print()

}
