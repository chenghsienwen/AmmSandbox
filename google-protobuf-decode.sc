import coursierapi.{MavenRepository}

interp.repositories() ++= Seq(
  MavenRepository
    .of("http://nexus.vpon.com/content/repositories/releases")
)
interp.load.ivy("com.quadas"                 %% "dsp-agent-model"            % "1.0.2+191-52ccf765")

@
import protobuf.comm.doubleclick.{BidRequest, BidResponse}
import java.nio.file.{Files, Paths}

val file = "testGoogleResponse_protobufEncode"
val byteArray = Files.readAllBytes(Paths.get(file))
val result = BidResponse.parseFrom(byteArray)
println(s"decoded ${result}")
