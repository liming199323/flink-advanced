package io.gourd.flink.scala.api

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/** Stream 任务
  *
  * @author Li.Wei by 2019/11/7
  */
trait StreamExecutionEnvironmentApp
  extends MainApp {
  val sEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
}
