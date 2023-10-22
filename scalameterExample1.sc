import $ivy.`com.storm-enroute::scalameter:0.17`

import org.scalameter._
(0 to 20).foreach { i => 
    val time = measure {
        (0 to 10000000).toArray
    }
    
    println(s"$i: Array initialization time: $time")
}

