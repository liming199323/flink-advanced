package io.gourd.flink.scala.batch.graph.util

/**
  * @author Li.Wei by 2019/10/29
  */
object ConnectedComponentsData {

  val VERTICES = Array[Long](1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)

  val EDGES = Array[Array[Long]](
    Array[Long](1L, 2L),
    Array[Long](2L, 3L),
    Array[Long](2L, 4L),
    Array[Long](3L, 5L),
    Array[Long](6L, 7L),
    Array[Long](8L, 9L),
    Array[Long](8L, 10L),
    Array[Long](5L, 11L),
    Array[Long](11L, 12L),
    Array[Long](10L, 13L),
    Array[Long](9L, 14L),
    Array[Long](13L, 14L),
    Array[Long](1L, 15L),
    Array[Long](16L, 1L)
  )
}
