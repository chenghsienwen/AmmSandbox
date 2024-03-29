import $ivy.`io.kamon::kamon-core:2.0.1`
import $ivy.`io.kamon::kamon-prometheus:2.0.1`
import $ivy.`io.kamon::kamon-system-metrics:2.0.1`

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


val configText =     """
       kamon {
           metric.tick-interval = 1 seconds
            modules.prometheus-reporter.enabled = false
            prometheus.start-embedded-http-server = false
            system-metrics {
            #sigar is enabled by default
            sigar-enabled = true

            #jmx related metrics are enabled by default
            jmx-enabled = true
            }
       }
    """

val config = parseString(configText)

 Kamon.init(config)
val reporter               = new PrometheusReporter
Kamon.registerModule("PrometheusReporter", reporter)

val gauge = Kamon.gauge("default-value").withoutTags()


gauge.update(11)
val time = 10
(0 to time).map{ i =>
    
    
    gauge.increment()
    Thread.sleep(1000)
    // println(s"setting ${Kamon.status().settings()}")
    // println(s"status ${Kamon.status().moduleRegistry()} ${Kamon.status().metricRegistry()} ${Kamon.status().instrumentation()}")
    println(s"$i   ${reporter.scrapeData()}")
    i
}







 