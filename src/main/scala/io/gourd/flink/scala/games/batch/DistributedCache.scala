package io.gourd.flink.scala.games.batch

import java.io.File

import io.gourd.flink.scala.api.BatchExecutionEnvironmentApp
import io.gourd.flink.scala.games.data.GameData.DataSet
import io.gourd.flink.scala.games.data.pojo.UserLogin
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration

import scala.io.BufferedSource


/** 分布式缓存
  * https://ci.apache.org/projects/flink/flink-docs-release-1.9/zh/dev/batch/#distributed-cache
  *
  * - Flink提供了一个分布式缓存，类似于hadoop，可以使用户在并行函数中很方便的读取本地文件，并把它放在 taskManager 节点中，防止task重复拉取
  *
  * - 此缓存的工作机制如下
  * 程序注册一个文件或者目录(本地或者远程文件系统，例如 hdfs 或者s3)，通过ExecutionEnvironment注册缓存文件并为它起一个名称
  * 当程序执行，Flink自动将文件或者目录复制到所有 taskManager 节点的本地文件系统，仅会执行一次。
  * 用户可以通过这个指定的名称查找文件或者目录，然后从 taskManager 节点的本地文件系统访问它
  *
  * =示例说明=
  * 分布式载入用户ID黑名单文件，针对用户登录数据匹配在黑名单ID及对应登录状态
  *
  * @author Li.Wei by 2019/11/4
  */
object DistributedCache extends BatchExecutionEnvironmentApp {

  private val path = getClass.getClassLoader.getResource("data/game/blacklist-uid.txt").getPath
  bEnv.registerCachedFile(path, "blacklist-uid", executable = false)

  // 用户登录数据 DataSet
  val userLoginDataSet = DataSet.userLogin(this)

  import org.apache.flink.api.scala.extensions._ // use filterWith

  userLoginDataSet
    .map(new RichMapFunction[UserLogin, (String, String)] {
      var source: BufferedSource = _
      var blackUid: Seq[String] = _

      override def open(config: Configuration): Unit = {
        val file: File = getRuntimeContext.getDistributedCache.getFile("blacklist-uid")
        import scala.io.Source
        source = Source.fromFile(file, "UTF-8")
        blackUid = source.getLines().toSeq
      }

      // 判断当前用户对应的ID在该用户对应角色中是否登录过
      override def map(value: UserLogin): (String, String) =
        if (blackUid.contains(value.uid)) (value.uid, value.status) else ("none", value.status)

      override def close(): Unit = source.close()
    })
    .filterWith(_._1 != "none")
    .distinct()
    .print()

}
