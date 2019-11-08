package io.gourd.flink.scala.games.typeinfo

import io.gourd.flink.scala.api.MainApp
import org.apache.flink.table.api.DataTypes

/**
  * @author Li.Wei by 2019/11/8
  */
object DataTypeApp extends MainApp {

  val time = DataTypes.TIME()

  info(s"${time.getLogicalType}") // TIME(0)
  info(s"${time.getConversionClass}") // class java.time.LocalTime

}
