package io.gourd.flink.scala.games.data

import org.apache.flink.api.scala.DataSet
import org.apache.flink.streaming.api.functions.source.SourceFunction

/**
  * 模拟实时数据流
  *
  * @param ds     静态数据流
  * @param millis 发射数据流时间间隔
  * @tparam T model
  */
class GameSourceFunction[T](ds: DataSet[T], millis: Long = 0) extends SourceFunction[T] {

  private val seq = ds.collect()
  private var counter = 0
  private var isRunning = true

  override def run(ctx: SourceFunction.SourceContext[T]): Unit = {
    while (isRunning && counter < seq.length) {
      ctx.collect(seq(counter))
      counter = counter + 1
      Thread.sleep(millis)
    }
  }

  override def cancel(): Unit = {
    isRunning = false
  }
}
