package io.gourd.flink.scala.games.data.pojo

/** 角色商城消费
  *
  * @author Li.Wei by 2019/11/6
  */
case class RoleShop(platform: String, server: String,
                    uid: String,
                    rid: String,
                    dataUnix: Int,
                    itemId: String, num: Long, totalMoney: Double) extends GameModel
