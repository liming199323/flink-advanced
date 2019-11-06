package io.gourd.flink.scala.games.data

import io.gourd.flink.scala.games.data.pojo.GameModel
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor

/**
  * @author Li.Wei by 2019/11/6
  */
// 自定义 eventTime
// API :stream.assignTimestampsAndWatermarks
class GameAscendingTimestampExtractor[T <: GameModel] extends AscendingTimestampExtractor[T] {
  override def extractAscendingTimestamp(element: T): Long = element.dataUnix
}
