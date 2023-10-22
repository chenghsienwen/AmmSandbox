interp.load.ivy("com.github.tototoshi" %% "scala-csv" % "1.3.6")

@
import com.github.tototoshi.csv._
import java.io._
import scala.io.Source

 val reader = CSVReader.open(new File("pixiv-net-sample.csv"))

 val result = reader.allWithHeaders()
println(s"result $result")
 reader.close()

 