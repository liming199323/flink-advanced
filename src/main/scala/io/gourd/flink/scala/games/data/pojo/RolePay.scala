package io.gourd.flink.scala.games.data.pojo

/** 角色充值
  *
  * @author Li.Wei by 2019/11/6
  */
case class RolePay(platform: String, server: String,
                   uid: String,
                   rid: String,
                   dataUnix: Int,
                   payWay: String, orderId: String, valid: String, money: Double) extends GameModel

