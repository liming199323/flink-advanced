package io.gourd.flink.scala.api

import org.apache.flink.table.api.scala.StreamTableEnvironment

/** Stream Table 任务
  *
  * @author Li.Wei by 2019/11/7
  */
trait StreamTableEnvironmentApp
  extends StreamExecutionEnvironmentApp
    with TableEnvironmentApp {

  val stEnv: StreamTableEnvironment = StreamTableEnvironment.create(sEnv)
  override val tEnv = stEnv

}
