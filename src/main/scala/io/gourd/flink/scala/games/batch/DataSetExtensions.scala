package io.gourd.flink.scala.games.batch

import io.gourd.flink.scala.MainApp
import io.gourd.flink.scala.games.Games.dataSetFromUserLogin
import org.apache.flink.api.scala.{ExecutionEnvironment, _}

/**
  * 函数扩展，支持偏函数应用
  * https://ci.apache.org/projects/flink/flink-docs-release-1.9/dev/scala_api_extensions.html#dataset-api
  *
  * @author Li.Wei by 2019/11/4
  */
object DataSetExtensions extends MainApp {
  val env = ExecutionEnvironment.getExecutionEnvironment

  // 用户登录数据 DataSet
  val userLoginDataSet = dataSetFromUserLogin(env)

  import org.apache.flink.api.scala.extensions._ // 引入 API 以支持 funWith

  userLoginDataSet
    .map(o => (o.platform, o.status))
    .mapWith {
      case (_, status) => status
    }.print()

}
