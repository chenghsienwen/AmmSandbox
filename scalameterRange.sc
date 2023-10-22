import $ivy.`com.storm-enroute::scalameter:0.17`

import org.scalameter.api._

object RangeBenchmark extends Bench.LocalTime {
    @main
    def main():Unit = {
        val sizes = Gen.range("size")(300000, 1500000, 300000)

        val ranges = for {
            size <- sizes
        } yield 0 until size

        val result = performance of "Range" in {
           measure method "map" in {
                using(ranges) in {
                    r => r.map(_ + 1)
                }
            }
        }
        println(s"result: $result")
    }
  
}
RangeBenchmark.main()