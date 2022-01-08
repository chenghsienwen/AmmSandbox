import coursierapi.{MavenRepository}

interp.repositories() ++= Seq(
  MavenRepository
    .of("http://nexus.vpon.com/content/repositories/releases"),
    MavenRepository
    .of("http://nexus.vpon.com/content/groups/public/")
)
// interp.load.ivy("com.quadas"                 %% "dsp-agent-model"            % "1.0.2+191-52ccf765")
interp.load.ivy("com.quadas"                 %% "dsp-agent-model"            % "1.0.2+329-f3a8a5d8")
@
import java.nio.file.{Files, Paths}
import protobuf.comm.Winnotice

// val test1 = Winnotice(
//             cid    = "cid",
//             served = "true",
//             price  = "AAAAAAAAAAAAAAAAAAAAAKuVQJubKcOC9IuFOw\u003d\u003d",
//             Winnotice.PriceType.CPCV,
//             Some("1641544737493")
//           )
//raw message from kafka
val test1 ="\nwd.t9qunq7d1ZVmodWKazvFsqz7lp-U0Drb4_TVuvC0p2hkMEU0MDgxNUEtRTYwOC00OTY0LTk3N0ItOTcyNEQ5ODVGOTAzBAMA0NDPMgKObVRZsczL.2563\u0012\u0004true\u001a(AAAAAAAAAAAAAAAAAAAAAKuVQJubKcOJfrU0QQ== \u0005*\r1641537230374"

val byteArray = test1.getBytes

// println(byteArray.toString)
val result = Winnotice.parseFrom(byteArray)
println(s"decoded ${result.toProtoString}")




