

// import org.apache.kafka.clients.consumer.{ConsumerRecord,ConsumerConfig}
// //import org.apache.kafka.common.serialization.{ByteArrayDeserializer, ByteArraySerializer, StringDeserializer, StringSerializer}


// import org.apache.kafka.common.serialization.StringDeserializer
// import org.apache.spark.streaming.kafka010._
// import org.apache.spark.SparkConf

// import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
// import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

// import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
// import org.apache.spark.streaming._
// //import org.apache.spark.streaming.kafka._
// import org.apache.spark.streaming.{Seconds, StreamingContext, Minutes}

// object stream{
//    def main(args:Array[String]){
//       val conf= new SparkConf().setMaster("local[2]").setAppName("KafkaWordCount")
//       val ssc=new StreamingContext(conf,Seconds(1))
//       ssc.checkpoint("checkpoint")
//       val topics=List("mytopic").toSet
//       //println("----------------------------------------------1")
//       val kafkaParams=Map[String,Object](
//          ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
//          ConsumerConfig.GROUP_ID_CONFIG -> "mygroup",
//          ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
//          ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
//          ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "latest",
//          ConsumerConfig.  ENABLE_AUTO_COMMIT_CONFIG -> (false: java.lang.Boolean)
//          )

//       val messages=KafkaUtils.createDirectStream[String, String](
//         ssc,LocationStrategies.PreferConsistent, 
//         ConsumerStrategies.Subscribe[String,String](topics,kafkaParams)
//       )
//        // println("----------------------------------------------2")
//        // messages.print()
//        // println("----------------------------------------------3")
//       val lines= messages.map(_.value)
//       lines.print()
//       //println("----------------------------------------------4")
//       val words= lines.flatMap(_.split(" "))
//       // words.print()
//       // println("----------------------------------------------5")
//       val pairs= words.map(x => (x,1))
//       // words.print()
//       // println("----------------------------------------------6")
//       val wordCounts=pairs.reduceByKey(_+_)
//       wordCounts.print()
//       // println("----------------------------------------------7")



//       ssc.start()
//       ssc.awaitTermination()

//    }
// }

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._

/**
 * Consumes messages from one or more topics in Kafka and does wordcount.
 * Usage: DirectKafkaWordCount <brokers> <topics>
 *   <brokers> is a list of one or more Kafka brokers
 *   <groupId> is a consumer group name to consume from topics
 *   <topics> is a list of one or more kafka topics to consume from
 *
 * Example:
 *    $ bin/run-example streaming.DirectKafkaWordCount broker1-host:port,broker2-host:port \
 *    consumer-group topic1,topic2
 */
object DirectKafkaWordCount {
  def main(args: Array[String]): Unit = {
    if (args.length < 3) {
      System.err.println(s"""
        |Usage: DirectKafkaWordCount <brokers> <groupId> <topics>
        |  <brokers> is a list of one or more Kafka brokers
        |  <groupId> is a consumer group name to consume from topics
        |  <topics> is a list of one or more kafka topics to consume from
        |
        """.stripMargin)
      System.exit(1)
    }

    //StreamingExamples.setStreamingLogLevels()

    val Array(brokers, groupId, topics) = args

    // Create context with 2 second batch interval
    val sparkConf = new SparkConf().setAppName("DirectKafkaWordCount").setMaster("local[1]")
    val ssc = new StreamingContext(sparkConf, Seconds(2))

    // Create direct kafka stream with brokers and topics
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer])

    val messages = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topicsSet, kafkaParams))

    // Get the lines, split them into words, count the words and print
    val lines = messages.map(_.value)
    // val words = lines.flatMap(_.split(" "))
    // val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)
    // wordCounts.print()
    lines.print()
    //lines.saveAsTextFiles("/home/sayukta/Desktop/IITM-Courses/Cloud/Assgn_5/Text_files/save_file1","txt")
    lines.saveAsTextFiles("/home/ubuntu/Text_files/save_file1","txt")
    
    ssc.start()
    ssc.awaitTermination()
  }
}
// scalastyle:on println
