package io.gourd.flink.scala.games.batch

import io.gourd.flink.scala.api.MainApp
import org.apache.flink.api.common.functions.RichFilterFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.configuration.Configuration

/** 自定义参数在函数中运用
  * https://ci.apache.org/projects/flink/flink-docs-release-1.9/dev/batch/#passing-parameters-to-functions
  *
  * [[UseConstructor]]    构造函数传递
  * [[UseWithParameters]] withParameters 方法传递 支持传递给 UdfOperator||DataSource
  * [[UseGlobally]]       setGlobalJobParameters 全局任务参数传递
  *
  * @author Li.Wei by 2019/11/4
  */
trait Parameters extends MainApp {
  val env = ExecutionEnvironment.getExecutionEnvironment
  val toFilter = env.fromElements(1, 2, 3)
}

/**
  * 构造函数传递
  */
object UseConstructor extends Parameters {

  toFilter
    .filter(_ > 2) // 2 可以由构造函数传递
    .print()

}

/**
  * RichFunction函数传递
  * 自定义参数传递给 [[org.apache.flink.api.common.functions.RichFunction]]
  */
object UseWithParameters extends Parameters {

  val c = new Configuration()
  c.setInteger("limit", 2)

  toFilter.filter(new RichFilterFunction[Int]() {
    var limit = 0

    override def open(config: Configuration): Unit = {
      limit = config.getInteger("limit", 0)
    }

    def filter(in: Int): Boolean = in > limit
  }).withParameters(c) // 自定义参数传递给 UdfOperator&DataSource
    .print()
}

/**
  * 全局参数传递
  */
object UseGlobally extends Parameters {
  val conf = new Configuration()
  conf.setInteger("limit", 2)
  env.getConfig.setGlobalJobParameters(conf) // 设置全局参数

  toFilter.filter(new RichFilterFunction[Int]() {
    var limit = 0

    override def open(config: Configuration): Unit = {
      val globalParams = getRuntimeContext.getExecutionConfig.getGlobalJobParameters

      // 从全局参数中获取对应值
      limit = globalParams.asInstanceOf[Configuration].getInteger("limit", limit)
    }

    def filter(in: Int): Boolean = in > limit
  })
    .print()
}