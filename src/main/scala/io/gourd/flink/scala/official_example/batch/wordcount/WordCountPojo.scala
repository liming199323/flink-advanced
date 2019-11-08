package io.gourd.flink.scala.official_example.batch.wordcount

import java.util.Locale

import io.gourd.flink.scala.official_example.data.Words
import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
import org.slf4j.LoggerFactory

/** 切分单词后封装为对象对单词数量统计
  *
  * @author Li.Wei by 2019/10/29
  */
object WordCountPojo {
  private val LOGGER = LoggerFactory.getLogger("WordCountPojo")

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val params = ParameterTool.fromArgs(args)
    // set up the execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment
    // make parameters available in the web interface
    env.getConfig.setGlobalJobParameters(params)
    // get input data
    val text = if (params.has("input")) env.readTextFile(params.get("input"))
    else { // get default test text data
      LOGGER.info("Executing WordCount example with default input data set.")
      LOGGER.info("Use --input to specify file input.")
      Words.dataSet(env)
    }
    val counts = text
      .flatMap(new Tokenizer())
      .groupBy(_.key)
      .reduce((w1, w2) => {
        Word(w1.key, w1.frequency + w2.frequency)
      })
    if (params.has("output")) {
      counts.writeAsText(params.get("output"), WriteMode.OVERWRITE)
      // execute program
      env.execute("WordCount-Pojo Example")
    } else {
      LOGGER.info("Printing result to stdout. Use --output to specify output path.")
      counts.print()
    }
  }

  // *************************************************************************
  //     USER FUNCTIONS
  // *************************************************************************

  /**
    * Implements the string tokenizer that splits sentences into words as a user-defined
    * FlatMapFunction. The function takes a line (String) and splits it into
    * multiple Word objects.
    */
  final class Tokenizer extends FlatMapFunction[String, Word] {
    override def flatMap(value: String, out: Collector[Word]): Unit = { // normalize and split the line
      val tokens = value.toLowerCase(Locale.getDefault).split("\\W+")
      for (token <- tokens) {
        if (token.length > 0) out.collect(Word(token, 1))
      }
    }
  }

}

case class Word(key: String, frequency: Long)
