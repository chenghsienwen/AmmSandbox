import $ivy.`io.kamon::kamon-core:2.1.0`
import $ivy.`io.kamon::kamon-prometheus:2.1.0`
// import $ivy.`io.kamon::kamon-system-metrics:2.0.1`

import $ivy.`com.typesafe:config:1.3.1`

import scala.collection.JavaConverters._

import com.typesafe.config.{Config, ConfigFactory, ConfigParseOptions}
import kamon.Kamon
import kamon.prometheus.PrometheusReporter

def parseOption(): ConfigParseOptions = {
    val classloader = Thread.currentThread().getContextClassLoader
    if (classloader eq null) throw new RuntimeException("No ClassLoader for current thread")

    ConfigParseOptions.defaults()
      .setClassLoader(classloader)
}
val options: ConfigParseOptions = parseOption()

val defaultConfig = ConfigFactory.load("kamon")

def parseString(input: String): Config = ConfigFactory.parseString(input, options).resolve().withFallback(defaultConfig)


val configText =    s"""
           |kamon.influxdb {
           |  hostname = ${influxDB.getHostName}
           |  port = ${influxDB.getPort}
           |
           |  environment-tags {
           |    include-service = no
           |    include-host = no
           |    include-instance = no
           |
           |    exclude = [ "env", "context" ]
           |  }
           |}
      """.stripMargin

val config = parseString(configText)

 Kamon.init(config)
val reporter               = new InfluxDBReporter()
Kamon.registerModule("InfluxDBReporter", reporter)

val counter = Kamon.counter("default-value").withoutTags()


counter.increment(11)
val time = 10
(0 to time).map{ i =>
    Thread.sleep(1000)
    
    
    // println(s"setting ${Kamon.status().settings()}")
    // println(s"status ${Kamon.status().moduleRegistry()} ${Kamon.status().metricRegistry()} ${Kamon.status().instrumentation()}")

    println(s"$i   ${reporter.scrapeData()}")
    counter.increment()
    i
}







 