package io.gourd.flink.scala.games.data.pojo

/** 用户登录
  *
  * @author Li.Wei by 2019/11/6
  */
case class UserLogin(platform: String, server: String,
                     uid: String,
                     dataUnix: Int,
                     status: String) extends GameModel
