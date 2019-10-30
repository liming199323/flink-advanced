package io.gourd.flink.scala

import io.gourd.flink.scala.games.Games.GameModel
import org.apache.flink.api.scala.DataSet
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor

/** 该包内所有数据集采用模拟游戏数据，表及字段参考 resources/data/game/README.MD 说明
  *
  * @author Li.Wei by 2019/10/29
  */
package object games


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

// 自定义 eventTime
// API :stream.assignTimestampsAndWatermarks
class GameAscendingTimestampExtractor[T <: GameModel] extends AscendingTimestampExtractor[T] {
  override def extractAscendingTimestamp(element: T): Long = element.dataUnix
}