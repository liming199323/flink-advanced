/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.gourd.flink.scala.official_example.batch.wordcount

import io.gourd.flink.scala.official_example.data.Words
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala._

/**
  * 切分单词后对单词数量统计
  *
  * Implements the "WordCount" program that computes a simple word occurrence histogram
  * over text files.
  *
  * The input is a plain text file with lines separated by newline characters.
  *
  * Usage:
  * {{{
  *   WordCount --input <path> --output <path>
  * }}}
  *
  * If no parameters are provided, the program is run with default data from
  * [[Words]]
  *
  * This example shows how to:
  *
  *   - write a simple Flink program.
  *   - use Tuple data types.
  *   - write and use user-defined functions.
  *
  */
object WordCount {

  def main(args: Array[String]) {

    val params: ParameterTool = ParameterTool.fromArgs(args)

    // set up execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment

    // make parameters available in the web interface
    env.getConfig.setGlobalJobParameters(params)

    // batch - DataSet type
    val text: DataSet[String] = if (params.has("input")) env.readTextFile(params.get("input"))
    else {
      println("Executing WordCount example with default input data set.")
      println("Use --input to specify file input.")
      Words.dataSet(env)
    }

    val counts = text.flatMap {
      _.split("\\W+").filter(_.nonEmpty)
    }
      .map {
        (_, 1)
      }
      .groupBy(0)
      .sum(1)

    if (params.has("output")) {
      counts.writeAsCsv(params.get("output"), "\n", " ")
      env.execute("Scala WordCount Example")
    } else {
      println("Printing result to stdout. Use --output to specify output path.")
      counts.print()
    }

  }
}


