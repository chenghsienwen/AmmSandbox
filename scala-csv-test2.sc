interp.load.ivy("com.github.tototoshi" %% "scala-csv" % "1.3.6")
interp.load.ivy("org.json4s" %% "json4s-core" % "3.6.10")
interp.load.ivy("org.json4s" %% "json4s-jackson" % "3.6.10")
@
import com.github.tototoshi.csv._
import java.io._
import scala.io.Source
import org.json4s._
import org.json4s.jackson.JsonMethods._
import scala.util.{Try, Success, Failure}

val m = Map(".name" -> "\"joe\"", ".address.street[0].aa.bb[0].cc" -> "\"Bulevard\"", ".address.city"   -> "\"Helsinki\"")

val test1 = Extraction.unflatten(m)
println(pretty(test1))

val reader = CSVReader.open(new File("pixiv-net-sample.csv"))

 val list = reader.allWithHeaders()
// println(s"result $list")
list.map{ item =>
    val nomalizedItem = item.filterNot(i => i._2 == "NULL").map{ 
        i => 
            Try(i._2.toDouble) match {
                case Success(v) => {
                    i._1.split("\\.").last.startsWith("is") match {
                        case true => s".${i._1}" -> (v == 1).toString
                        case false => s".${i._1}" -> i._2
                    }
                }
                case Failure(ex) => s".${i._1}" ->( "\"" + i._2 + "\"")
            }
    }
    println(s"nomalizedItem $nomalizedItem")
    val json = Extraction.unflatten(nomalizedItem)
    println(s"json ${pretty(json)}")
}

 reader.close()


 