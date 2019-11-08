package io.gourd.flink.scala.api

import org.apache.flink.api.scala.ExecutionEnvironment

/** Batch 任务
  *
  * @author Li.Wei by 2019/11/6
  */
trait BatchExecutionEnvironmentApp extends MainApp {

  val bEnv: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

}
