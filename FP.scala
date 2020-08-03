/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println

import scopt.OptionParser

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.fpm.FPGrowth

import scala.reflect.runtime.universe._

import java.io._



/**
 * Abstract class for parameter case classes.
 * This overrides the [[toString]] method to print all case class fields by name and value.
 * @tparam T  Concrete parameter class.
 */
abstract class AbstractParams[T: TypeTag] {

  private def tag: TypeTag[T] = typeTag[T]

  /**
   * Finds all case class fields in concrete class instance, and outputs them in JSON-style format:
   * {
   *   [field name]:\t[field value]\n
   *   [field name]:\t[field value]\n
   *   ...
   * }
   */
  override def toString: String = {
    val tpe = tag.tpe
    val allAccessors = tpe.decls.collect {
      case m: MethodSymbol if m.isCaseAccessor => m
    }
    val mirror = runtimeMirror(getClass.getClassLoader)
    val instanceMirror = mirror.reflect(this)
    allAccessors.map { f =>
      val paramName = f.name.toString
      val fieldMirror = instanceMirror.reflectField(f)
      val paramValue = fieldMirror.get
      s"  $paramName:\t$paramValue"
    }.mkString("{\n", ",\n", "\n}")
  }
}

/**
 * Example for mining frequent itemsets using FP-growth.
 * Example usage: ./bin/run-example mllib.FPGrowthExample \
 *   --minSupport 0.8 --numPartition 2 ./data/mllib/sample_fpgrowth.txt
 */
object FP {

  case class Params(
    input: String = null,
    minSupport: Double = 0.2,
    numPartition: Int = 2) extends AbstractParams[Params]

  def main(args: Array[String]): Unit = {
    val defaultParams = Params()

    val parser = new OptionParser[Params]("FPGrowthExample") {
      head("FPGrowth: an example FP-growth app.")
      opt[Double]("minSupport")
        .text(s"minimal support level, default: ${defaultParams.minSupport}")
        .action((x, c) => c.copy(minSupport = x))
      opt[Int]("numPartition")
        .text(s"number of partition, default: ${defaultParams.numPartition}")
        .action((x, c) => c.copy(numPartition = x))
      arg[String]("<input>")
        .text("input paths to input data set, whose file format is that each line " +
          "contains a transaction with each item in String and separated by a space")
        .required()
        .action((x, c) => c.copy(input = x))
    }

    parser.parse(args, defaultParams) match {
      case Some(params) => run(params)
      case _ => sys.exit(1)
    }
  }

  def run(params: Params): Unit = {
    val conf = new SparkConf().setAppName(s"FPGrowthExample with $params")
    conf.set("spark.master","local");
    conf.set("spark.app.name","app2");
    val sc = new SparkContext(conf)
    val transactions = sc.textFile(params.input).map(_.split(",")).cache()

    println(s"Number of transactions: ${transactions.count()}")

    val model = new FPGrowth()
      .setMinSupport(params.minSupport)
      .setNumPartitions(params.numPartition)
      .run(transactions)

    println(s"Number of frequent itemsets: ${model.freqItemsets.count()}")

    //val pw = new PrintWriter(new File("/home/sayukta/Desktop/IITM-Courses/Cloud/Assgn_5/fp_output" ))
    val pw = new PrintWriter(new File("/home/ubuntu/fp_output" )) 

    model.freqItemsets.collect().foreach { itemset =>
      println(s"${itemset.items.mkString("[", ",", "]")}, ${itemset.freq}")
      pw.write(s"${itemset.items.mkString("[", ",", "]")}, ${itemset.freq}\n")
    }
    pw.close()
    sc.stop()
  }
}
// scalastyle:on println
