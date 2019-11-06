package io.gourd.flink.scala.data

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.streaming.api.scala._

import scala.io.Source

/** 数据集
  * 内容参考 data/words/words.txt
  *
  * @author Li.Wei by 2019/10/29
  */
object Words {

  val WORDS: String = {
    val source = Source.fromURL(ClassLoader.getSystemResource("data/words/words.txt"), "UTF-8")
    val lines = source.mkString
    source.close()
    lines
  }

  def dataSet(env: ExecutionEnvironment): DataSet[String] = env.fromElements(WORDS)

  def dataStream(env: StreamExecutionEnvironment): DataStream[String] = env.fromElements(WORDS)
}
