import coursierapi.{MavenRepository}

interp.repositories() ++= Seq(
  MavenRepository
    .of("http://nexus.vpon.com/content/repositories/releases")
)

interp.load.ivy("com.quadas"                 % "dsp-protobuf-java"            % "245_ecfd7c5")
interp.load.ivy("com.google.protobuf"   % "protobuf-java-util" % "3.10.0")
@
import protobuf.comm.vponadn.OpenRtb.BidRequest
import scala.util.Try

val bid = BidRequest.newBuilder()

bid.setId("Id")
Try(bid.setId(null))
bid.build()

println(s"result $bid")
