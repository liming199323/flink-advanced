package io.gourd.flink.scala.games.data

import io.gourd.flink.scala.api._
import io.gourd.flink.scala.games.data.pojo.{GameModel, RoleLogin, RolePay, RoleShop, UserLogin}
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.io.TupleCsvInputFormat
import org.apache.flink.api.java.typeutils.TupleTypeInfoBase
import org.apache.flink.api.scala.typeutils.{CaseClassTypeInfo, Types}
import org.apache.flink.api.scala.{DataSet, _}
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.table.api.Table
import org.apache.flink.table.expressions.Expression
import org.apache.flink.table.sources.TableSource

/** 使用 resource/data/game/ 作为数据源
  *
  * @author Li.Wei by 2019/10/29
  */
object GameData {

  val userLoginCsvPath = getClass.getClassLoader.getResource("data/game/UserLogin.csv").getPath
  val roleLoginCsvPath = getClass.getClassLoader.getResource("data/game/RoleLogin.csv").getPath
  val rolePayCsvPath = getClass.getClassLoader.getResource("data/game/RolePay.csv").getPath
  val roleShopCsvPath = getClass.getClassLoader.getResource("data/game/RoleShop.csv").getPath


  // load DataSet
  object DataSet {
    def userLogin(app: BatchExecutionEnvironmentApp): DataSet[UserLogin] =
      app.bEnv.readCsvFile[UserLogin](
        userLoginCsvPath,
        fieldDelimiter = ",",
        ignoreFirstLine = true
      )

    def roleLogin(app: BatchExecutionEnvironmentApp): DataSet[RoleLogin] =
      app.bEnv.readCsvFile[RoleLogin](
        roleLoginCsvPath,
        fieldDelimiter = ",",
        ignoreFirstLine = true
      )

    def rolePay(app: BatchExecutionEnvironmentApp): DataSet[RolePay] =
      app.bEnv.readCsvFile[RolePay](
        rolePayCsvPath,
        fieldDelimiter = ",",
        ignoreFirstLine = true
      )

    def roleShop(app: BatchExecutionEnvironmentApp): DataSet[RoleShop] =
      app.bEnv.readCsvFile[RoleShop](
        roleShopCsvPath,
        fieldDelimiter = ",",
        ignoreFirstLine = true
      )
  }

  // Register DataSet
  object RegisterDataSet {

    private def registerDs[T](app: BatchTableEnvironmentApp, tableName: String, ds: DataSet[T],
                              fields: Option[Array[Expression]]): String = {
      fields match {
        case Some(value) => app.btEnv.registerDataSet(tableName, ds, value.toSeq: _*)
        case None => app.btEnv.registerDataSet(tableName, ds)
      }
      tableName
    }

    def userLogin(app: BatchTableEnvironmentApp, fields: Option[Array[Expression]] = None): String =
      registerDs[UserLogin](app, "UserLogin", DataSet.userLogin(app), fields)

    def roleLogin(app: BatchTableEnvironmentApp, fields: Option[Array[Expression]] = None): String =
      registerDs[RoleLogin](app, "RoleLogin", DataSet.roleLogin(app), fields)

    def rolePay(app: BatchTableEnvironmentApp, fields: Option[Array[Expression]] = None): String =
      registerDs[RolePay](app, "RolePay", DataSet.rolePay(app), fields)

    def roleShop(app: BatchTableEnvironmentApp, fields: Option[Array[Expression]] = None): String =
      registerDs[RoleShop](app, "RoleShop", DataSet.roleShop(app), fields)
  }

  // load DataStream
  object DataStream {

    private def loadStream[T: TypeInformation](app: StreamExecutionEnvironmentApp, path: String): DataStream[T] = {
      val typeInfo = implicitly[TypeInformation[T]]
      val format = new TupleCsvInputFormat(new Path(path), typeInfo.asInstanceOf[TupleTypeInfoBase[T]])


      app.sEnv.createInput(format)
    }

    def userLogin(app: StreamExecutionEnvironmentApp): DataStream[UserLogin] =
      loadStream[UserLogin](app, userLoginCsvPath)

    def roleLogin(app: StreamExecutionEnvironmentApp): DataStream[RoleLogin] =
      loadStream[RoleLogin](app, roleLoginCsvPath)

    def rolePay(app: StreamExecutionEnvironmentApp): DataStream[RolePay] =
      loadStream[RolePay](app, rolePayCsvPath)

    def roleShop(app: StreamExecutionEnvironmentApp): DataStream[RoleShop] =
      loadStream[RoleShop](app, roleShopCsvPath)
  }

  // Register DataStream
  object RegisterDataStream {

    private def registerDs[T](app: StreamTableEnvironmentApp, tableName: String, ds: DataStream[T],
                              fields: Option[Array[Expression]]): String = {
      fields match {
        case Some(value) => app.stEnv.registerDataStream(tableName, ds, value.toSeq: _*)
        case None => app.stEnv.registerDataStream(tableName, ds)
      }

      tableName
    }

    def userLogin(app: StreamTableEnvironmentApp, fields: Option[Array[Expression]] = None): String =
      registerDs[UserLogin](app, "UserLogin", DataStream.userLogin(app), fields)

    def roleLogin(app: StreamTableEnvironmentApp, fields: Option[Array[Expression]] = None): String =
      registerDs[RoleLogin](app, "RoleLogin", DataStream.roleLogin(app), fields)

    def rolePay(app: StreamTableEnvironmentApp, fields: Option[Array[Expression]] = None): String =
      registerDs[RolePay](app, "RolePay", DataStream.rolePay(app), fields)

    def roleShop(app: StreamTableEnvironmentApp, fields: Option[Array[Expression]] = None): String =
      registerDs[RoleShop](app, "RoleShop", DataStream.roleShop(app), fields)
  }


  object Table {
    def aa(app: TableEnvironmentApp, source: TableSource[_]): Table = app.tEnv.fromTableSource(source)

    //    def userLogin(app: TableEnvironmentApp): Table = {
    //      TypeInformation.of(UserLogin.getClass).asInstanceOf[CaseClassTypeInfo].typeParamTypeInfos
    //      CsvTableSource.builder()
    //        .ignoreFirstLine()
    //        .path()
    //        .field()
    //          .build()
    //      app.tEnv.fromTableSource(new CsvTableSource())
    //    }

  }

}

object TypeInformationUse {
  def main(args: Array[String]): Unit = {
    val value = TypeInformation.of(classOf[UserLogin])
    println(value.getGenericParameters)

    val value1 = Types.of[UserLogin]
    println(value1.getClass)
    println(value1.getGenericParameters)

    val value2 =
      Types.CASE_CLASS[UserLogin].asInstanceOf[CaseClassTypeInfo[UserLogin]]
    println(value2.getClass)
    println(value2.getGenericParameters)


    val tuple = GameModel.fieldNameTypes[UserLogin]()
    println(tuple)

  }
}
