package io.gourd.flink.scala.games.batch

import io.gourd.flink.scala.api.BatchExecutionEnvironmentApp

/** 在某些算法中，可能需要为数据集元素分配唯一标识符。
  * 本文档说明了如何将
  * [[org.apache.flink.api.scala.utils.DataSetUtils]]
  * [[org.apache.flink.api.java.utils.DataSetUtils.zipWithIndex()]]
  * [[org.apache.flink.api.java.utils.DataSetUtils.zipWithUniqueId()]]
  * 用于此目的。
  *
  * @author Li.Wei by 2019/11/12
  */
object ZippingElements extends BatchExecutionEnvironmentApp {

  import org.apache.flink.api.scala._

  val input: DataSet[String] = bEnv.fromElements("A", "B", "C", "D", "E", "F", "G", "H")
  bEnv.setParallelism(2)

  /*
  zipWithIndex为元素分配连续的标签，接收数据集作为输入并返回 DataSet[(Long, T)] 2元组的新数据集。
  此过程需要两步操作，首先是计数，然后是标记元素，由于计数同步，因此无法进行流水线处理。
  替代方法zipWithUniqueId以流水线方式工作，当唯一的标签足够时，它是首选方法。
   */

  import org.apache.flink.api.scala.utils.DataSetUtils

  input.zipWithIndex.print()

  println()
  /*
  在许多情况下，可能不需要分配连续的标签。
  zipWithUniqueId以管道方式工作，加快了标签分配过程。
  此方法接收一个数据集作为输入，并返回一个新的 DataSet[(Long, T)] 2元组数据集
   */
  input.zipWithUniqueId.print()

}
