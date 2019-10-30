package io.gourd.flink.scala.games

import io.gourd.flink.scala.games.Games._
import io.gourd.flink.scala.{GameAscendingTimestampExtractor, GameSourceFunction, MainApp}
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.table.api.scala.{StreamTableEnvironment, _}

/** Streaming 任务 API 使用示例
  * [[StreamingDataStream]]
  * [[StreamingTable]]
  * [[StreamingSQL]]
  *
  * @author Li.Wei by 2019/10/30
  */
trait Streaming extends MainApp {

  val sEnv = StreamExecutionEnvironment.getExecutionEnvironment
  sEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

  val evn = ExecutionEnvironment.getExecutionEnvironment
  val userLoginDataStream: DataStream[UserLogin] = sEnv
    .addSource(new GameSourceFunction(dataSetFromUserLogin(evn), millis = 0))
    .assignTimestampsAndWatermarks(new GameAscendingTimestampExtractor[UserLogin]())

  val roleLoginDataStream = sEnv
    .addSource(new GameSourceFunction(dataSetFromRoleLogin(evn), millis = 0))
    .assignTimestampsAndWatermarks(new GameAscendingTimestampExtractor[RoleLogin]())

}

object StreamingDataStream extends Streaming {
  userLoginDataStream
    .filter(_.dataUnix > 0)
    .filter(_.status == "LOGIN")
    .join(roleLoginDataStream).where(_.uid).equalTo(_.uid)
    .window(TumblingEventTimeWindows.of(Time.milliseconds(3000)))
    .apply((x1, x2) => {
      (x1.platform, x1.uid, x2.rid)
    })
    .print()
  println(sEnv.getExecutionPlan)
  // sEnv.execute("StreamingDataStream")
}

object StreamingTable extends Streaming {

  val tEnv = StreamTableEnvironment.create(sEnv)
  val userLoginTable = tEnv.fromDataStream(userLoginDataStream)
  val roleLoginTable = tEnv.fromDataStream(roleLoginDataStream)

  userLoginTable
    .filter('dataUnix > 0)
    .filter('status === "LOGIN")
    .join(roleLoginTable.select("uid as r_uid, rid")).where("uid = r_uid")
    .select("platform,rid")
    .toAppendStream[(String, String)]
    .print()
  println(sEnv.getExecutionPlan)
  // tEnv.execute("StreamingTable")

}

object StreamingSQL extends Streaming {

  val tEnv = StreamTableEnvironment.create(sEnv)

  tEnv.registerDataStream("userLoginTable", userLoginDataStream)
  tEnv.registerDataStream("roleLoginTable", roleLoginDataStream)

  tEnv.sqlQuery(
    """
      |SELECT u.* FROM
      |(
      |SELECT * FROM userLoginTable
      |WHERE dataUnix > 0 AND status = 'LOGIN'
      |) u
      |JOIN
      |(SELECT uid as r_uid FROM roleLoginTable) r
      |ON(u.uid = r.r_uid)
      |""".stripMargin)
    .toAppendStream[UserLogin]
    .print()

  tEnv.execute("StreamingTable")
  println(sEnv.getExecutionPlan) // 打印执行计划 https://flink.apache.org/visualizer/index.html
}
