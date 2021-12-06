import coursierapi.{MavenRepository}

val circeVersion = "0.10.0"
interp.repositories() ++= Seq(
  MavenRepository
    .of("https://repository.cloudera.com/artifactory/libs-release-local")
)

interp.load.ivy("org.apache.hadoop" % "hadoop-client" % "2.0.0-mr1-cdh4.7.1")

@
import java.io.InputStream;
import java.net.URI;
 
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

val conf = new Configuration()
conf.set("fs.defaultFS","hdfs://localhost:9000")
val fs = FileSystem.get(conf)
val fsStatus = fs.listStatus(new Path("/user/hive/warehouse/4823_auto_target_clicker_2101_ip_c31815/dt=20210116_104113"))
println(fsStatus)
