package io.gourd.flink.scala.official_example.batch.graph.util

import java.{lang, util}

import org.apache.flink.api.java.ExecutionEnvironment
import org.apache.flink.api.java.operators.DataSource
import org.apache.flink.api.java.tuple.Tuple2

/**
  * @author Li.Wei by 2019/10/29
  */
object PageRankData {

  val EDGES = Array(
    Array(1L, 2L), Array(1L, 15L), Array(2L, 3L), Array(2L, 4L), Array(2L, 5L), Array(2L, 6L),
    Array(2L, 7L), Array(3L, 13L), Array(4L, 2L), Array(5L, 11L), Array(5L, 12L), Array(6L, 1L), Array(6L, 7L),
    Array(6L, 8L), Array(7L, 1L), Array(7L, 8L), Array(8L, 1L), Array(8L, 9L), Array(8L, 10L), Array(9L, 14L),
    Array(9L, 1L), Array(10L, 1L), Array(10L, 13L), Array(11L, 12L), Array(11L, 1L), Array(12L, 1L),
    Array(13L, 14L), Array(14L, 12L), Array(15L, 1L)
  )

  private val numPages = 15

  def getDefaultEdgeDataSet(env: ExecutionEnvironment): DataSource[Tuple2[Long, Long]] = {
    val edges = new util.ArrayList[Tuple2[Long, Long]]
    for (e <- EDGES) {
      edges.add(new Tuple2[Long, Long](e(0), e(1)))
    }
    env.fromCollection(edges)
  }

  def getDefaultPagesDataSet(env: ExecutionEnvironment): DataSource[lang.Long] = env.generateSequence(1, 15)

  def getNumberOfPages: Int = numPages

}
