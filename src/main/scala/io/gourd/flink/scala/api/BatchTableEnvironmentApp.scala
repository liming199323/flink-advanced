package io.gourd.flink.scala.api

import org.apache.flink.table.api.scala.BatchTableEnvironment

/**
  * @author Li.Wei by 2019/11/6
  */
trait BatchTableEnvironmentApp extends ExecutionEnvironmentApp {

  val tEnv: BatchTableEnvironment

}
