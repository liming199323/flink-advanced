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
package io.gourd.flink.scala.games

import io.gourd.flink.scala.data.Games
import org.apache.flink.api.scala._

/**
  * 数据载入示例
  */
object DataSetLoad {

  def main(args: Array[String]) {

    val env = ExecutionEnvironment.getExecutionEnvironment
    val ds = Games.dataSetFromUserLogin(env)
    ds.print()
    env.execute("Scala DataSetLoad Example")
  }


}
