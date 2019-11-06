package io.gourd.flink.scala

import io.gourd.flink.scala.api.{BatchTableEnvironmentApp, ExecutionEnvironmentApp, MainApp}
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.table.api.scala.BatchTableEnvironment

/**
  * @author Li.Wei by 2019/11/6
  */
trait BatchLocalApp extends MainApp with ExecutionEnvironmentApp {

  override val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
}

trait BatchTableLocalApp extends BatchLocalApp with BatchTableEnvironmentApp {

  override val tEnv: BatchTableEnvironment = BatchTableEnvironment.create(env)
}
