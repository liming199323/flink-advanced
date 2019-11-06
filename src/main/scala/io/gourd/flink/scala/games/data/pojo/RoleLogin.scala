package io.gourd.flink.scala.games.data.pojo

/** 角色登录
  *
  * @author Li.Wei by 2019/11/6
  */
case class RoleLogin(platform: String, server: String,
                     uid: String,
                     rid: String,
                     dataUnix: Int,
                     status: String) extends GameModel
