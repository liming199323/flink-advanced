package io.gourd.flink.scala.games.data.pojo

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.scala.typeutils.{CaseClassTypeInfo, Types}

/**
  * 模拟游戏数据结构化
  *
  * @author Li.Wei by 2019/11/6
  */
trait GameModel {

  /**
    * @return 事件事件/unix 秒时间戳
    */
  def getDataUnix: Int
}

object GameModel {
  /**
    * 获取指定 CaseClass 字段类型 , 保证结果有序
    *
    * @tparam T TypeInformation case class
    * @return 字段值、字段对应类型
    */
  def fieldNameTypes[T <: Product : TypeInformation](): List[(String, TypeInformation[_])] = {
    val ct = Types.CASE_CLASS[T].asInstanceOf[CaseClassTypeInfo[T]]
    ct.fieldNames.map(f => f -> ct.getTypeAt(f)).toList
  }
}