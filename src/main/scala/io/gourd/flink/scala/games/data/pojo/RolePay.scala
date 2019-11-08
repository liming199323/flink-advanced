package io.gourd.flink.scala.games.data.pojo

/** 角色充值
  *
  * @param platform 所在平台id（e.g. H5/IOS/ADR/IOS_YY）
  * @param server   所在游戏服id
  * @param uid      用户唯一id
  * @param rid      角色唯一id
  * @param dataUnix 事件时间/s 时间戳
  * @param payWay   支付方式
  * @param orderId  订单id
  * @param money    金额
  * @author Li.Wei by 2019/11/6
  */
case class RolePay(platform: String, server: String,
                   uid: String,
                   rid: String,
                   dataUnix: Int,
                   payWay: String, orderId: String, money: Double) extends GameModel {
  override def getDataUnix: Int = dataUnix
}

