
import $ivy.{
  `org.apache.spark::spark-core:2.4.4`
}

import org.apache.spark.{SparkConf, SparkContext}

val conf = new SparkConf().setAppName("test").setMaster("local[*]").set("spark.driver.allowMultipleContexts", "true")


val sc = new SparkContext(conf)
val result = sc.parallelize(1 to 5).map(_ * 10).collect()

println(s"Spark result: ${result.toList}")
assert(result.toList == List(10, 20, 30, 40, 50))