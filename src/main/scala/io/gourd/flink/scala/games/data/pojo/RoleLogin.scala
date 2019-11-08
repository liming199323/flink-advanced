package io.gourd.flink.scala.games.data.pojo

/** 角色登录
  *
  * @param platform 所在平台id（e.g. H5/IOS/ADR/IOS_YY）
  * @param server   所在游戏服id
  * @param uid      用户唯一id
  * @param rid      角色唯一id
  * @param dataUnix 事件时间/s 时间戳
  * @param status   登录动作（LOGIN/LOGOUT）
  * @author Li.Wei by 2019/11/6
  */
case class RoleLogin(platform: String, server: String,
                     uid: String,
                     rid: String,
                     dataUnix: Int,
                     status: String) extends GameModel {
  override def getDataUnix: Int = dataUnix
}
