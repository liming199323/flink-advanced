package io.gourd.flink.scala.api

import org.apache.flink.table.api.TableEnvironment

/** Table 任务
  * 抽象于 Batch 与 Stream。
  * 具体以 [[BatchTableEnvironmentApp]] [[StreamExecutionEnvironmentApp]] 实现为主
  *
  * @author Li.Wei by 2019/11/8
  */
trait TableEnvironmentApp {

  val tEnv: TableEnvironment
}
