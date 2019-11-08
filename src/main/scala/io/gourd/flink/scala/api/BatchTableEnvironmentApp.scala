package io.gourd.flink.scala.api

import org.apache.flink.table.api.scala.BatchTableEnvironment

/** Batch Table 任务
  *
  * @author Li.Wei by 2019/11/6
  */
trait BatchTableEnvironmentApp
  extends BatchExecutionEnvironmentApp
    with TableEnvironmentApp {

  val btEnv: BatchTableEnvironment = BatchTableEnvironment.create(bEnv)

  override val tEnv = btEnv

}
