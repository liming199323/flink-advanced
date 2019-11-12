package io.gourd.flink.scala.games.batch

import io.gourd.flink.scala.api.BatchExecutionEnvironmentApp
import io.gourd.flink.scala.games.data.GameData.DataSet
import io.gourd.flink.scala.games.data.pojo.UserLogin
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration

/** 广播变量
  * https://ci.apache.org/projects/flink/flink-docs-release-1.9/zh/dev/batch/#broadcast-variables
  *
  * @author Li.Wei by 2019/11/4
  */
object Broadcast extends BatchExecutionEnvironmentApp {

  // 用户登录数据 DataSet
  val userLoginDs = DataSet.userLogin(this)

  // 角色登录数据 DataSet 对应用户ID,去重
  val roleLoginDs = DataSet.roleLogin(this).map(_.uid).distinct()

  userLoginDs
    .map(new MyBroadcastMap())
    .withBroadcastSet(roleLoginDs, "roleLoginDataSet")
    .first(10)
    .print()
}

/**
  * 自定义 map 实现函数，RichMapFunction 中可获取flink上下文及执行前后的打开关闭操作
  */
class MyBroadcastMap extends RichMapFunction[UserLogin, (String, String)] {
  var broadcastSet: Traversable[String] = _

  override def open(config: Configuration): Unit = {
    // 3. Access the broadcast DataSet as a Collection
    import scala.collection.JavaConverters._
    broadcastSet = getRuntimeContext.getBroadcastVariable[String]("roleLoginDataSet").asScala
  }

  // 判断当前用户对应的ID在该用户对应角色中是否登录过
  override def map(value: UserLogin): (String, String) =
    if (broadcastSet.exists(_ == value.uid)) (value.uid, value.status) else ("none", value.status)

}