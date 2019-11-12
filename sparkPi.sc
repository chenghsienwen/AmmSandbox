import $ivy.`org.apache.spark::spark-sql:2.4.4`

import scala.math.random

import org.apache.spark.sql.SparkSession

/** Computes an approximation to pi */
@main
def main(slicesCount: Int): Unit = {
  val spark = SparkSession
    .builder.master("local").config("spark.driver.allowMultipleContexts", "true")
    .appName("Spark Pi")
    .getOrCreate()
  val slices = if (slicesCount > 0) slicesCount else 2
  val n = math.min(100000L * slices, Int.MaxValue).toInt // avoid overflow
  val count = spark.sparkContext.parallelize(1 until n, slices).map { i =>
    val x = random * 2 - 1
    val y = random * 2 - 1
    if (x*x + y*y <= 1) 1 else 0
  }.reduce(_ + _)
  println(s"Pi is roughly ${4.0 * count / (n - 1)}")
  spark.stop()
}


