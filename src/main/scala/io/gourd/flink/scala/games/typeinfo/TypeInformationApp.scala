package io.gourd.flink.scala.games.typeinfo

import io.gourd.flink.scala.api.MainApp
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.scala.typeutils.{CaseClassTypeInfo, Types}
import org.apache.flink.streaming.api.scala._

/** [[TypeInformation]] 数据类型使用示例
  *
  * @author Li.Wei by 2019/11/8
  */
object TypeInformationApp extends MainApp {

  // 获取 TypeInformation
  val appleType1 = TypeInformation.of(classOf[Apple])
  // implicit def createTypeInformation[T]: TypeInformation[T] = macro TypeUtils.createTypeInfo[T]
  val appleType2 = createTypeInformation[Apple]

  // case class 获取 TypeInformation
  val appleType = Types.CASE_CLASS[Apple].asInstanceOf[CaseClassTypeInfo[Apple]]
  info(s"${appleType.isCaseClass()}") // true

  // 泛型推导
  val tuple2 = Types.TUPLE[(String, Int)]
  info(s"${tuple2.getGenericParameters}") // {T1=String, T2=Integer}

  // 泛型推导
  val boxType = Types.CASE_CLASS[Box[Apple]].asInstanceOf[CaseClassTypeInfo[Box[Apple]]]
  info(s"${boxType.getGenericParameters()}") // {T1=Apple(color: String, size: Integer)}

  // 字段类型获取
  val fieldTypes = (boxType.fieldNames, boxType.fieldNames.map(boxType.getTypeAt))
  info(s"$fieldTypes")
  // (
  // List(content, maxWeight),
  // List(ObjectArrayTypeInfo<io.gourd.flink.scala.games.typeinfo.Apple(color: String, weight: Integer)>, Integer)
  // )
}

/**
  * 苹果
  *
  * @param color  颜色
  * @param weight 重量
  */
case class Apple(color: String, weight: Int)

/** 箱子
  *
  * @param content   内容，e.g. 内容是一箱子苹果
  * @param maxWeight 最大重量
  * @tparam T 箱子内内容描述
  */
case class Box[T](content: Array[T], maxWeight: Int)