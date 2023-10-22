import $ivy.`com.storm-enroute::scalameter:0.17`

import org.scalameter._
val config = 
(0 to 10).foreach { i => 
    val time = withWarmer(new Warmer.Default).measure {
        (0 to 10000000).toArray
    }
    
    println(s"$i: time: $time")
}

