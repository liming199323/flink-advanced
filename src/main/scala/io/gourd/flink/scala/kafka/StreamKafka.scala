package io.gourd.flink.scala.kafka

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.datastream.DataStreamSink
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

/**
  * @author Li.Wei by 2019/10/29
  */
object StreamKafka {

  def main(args: Array[String]): Unit = {

    // val env = StreamExecutionEnvironment.getExecutionEnvironment
    val env = StreamExecutionEnvironment.createRemoteEnvironment("localhost", 8081)

    val properties = new Properties
    properties.setProperty("bootstrap.servers", "skuldcdhtest1.ktcs:9092")
    properties.setProperty("zookeeper.connect", "skuldcdhtest1.ktcs:2181")
    properties.setProperty("group.id", "test")
    val myConsumer = new FlinkKafkaConsumer[String]("game_log_game_skuld_01", new SimpleStringSchema, properties)

    val stream: DataStreamSink[String] = env.addSource(myConsumer).print

    env.execute("StreamKafka")
  }
}
