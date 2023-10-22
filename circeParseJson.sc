import coursierapi.{MavenRepository}

val circeVersion = "0.10.0"
interp.repositories() ++= Seq(
  MavenRepository
    .of("http://nexus.vpon.com/content/groups/public")
)
interp.load.ivy("com.quadas"                 %% "dsp-agent-model"            % "1.0.2+210-63afa1a6")
interp.load.ivy( "io.circe" %% "circe-core" % circeVersion)
interp.load.ivy("io.circe" %% "circe-generic" % circeVersion)
interp.load.ivy("io.circe" %% "circe-parser" % circeVersion)
interp.load.ivy("io.circe" %% "circe-optics" % circeVersion)
@
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._
import io.circe.{Json, JsonObject}
import protobuf.comm.vponadn.BidRequest.Imp.{Banner, Pmp}
import protobuf.comm.vponadn.BidRequest.{App, Content, Device, Geo, Publisher, Site, User, _}
import protobuf.comm.vponadn.{BidRequest, BidResponse}
import protobuf.comm.vponadn._
val path = "sampleMyJson.json"
val text = scala.io.Source.fromFile(path).getLines.toList.mkString("\n")
val json = parse(text).right.get
implicit val encoder2: Encoder[Source] = deriveEncoder
implicit val encoder3: Encoder[Imp] = deriveEncoder
implicit val encoder4: Encoder[Metric] = deriveEncoder
implicit val encoder5: Encoder[Banner] = deriveEncoder
implicit val encoder6: Encoder[Format] = deriveEncoder
implicit val encoder7: Encoder[Video] = deriveEncoder
implicit val encoder8: Encoder[CompanionAd] = deriveEncoder
implicit val encoder9: Encoder[Audio] = deriveEncoder
implicit val encoder10: Encoder[Native] = deriveEncoder
implicit val encoder11: Encoder[Pmp] = deriveEncoder
implicit val encoder12: Encoder[Deal] = deriveEncoder
implicit val encoder13: Encoder[Site] = deriveEncoder
implicit val encoder14: Encoder[App] = deriveEncoder
implicit val encoder15: Encoder[Publisher] = deriveEncoder
implicit val encoder16: Encoder[Content] = deriveEncoder
implicit val encoder17: Encoder[Producer] = deriveEncoder
implicit val encoder18: Encoder[Device] = deriveEncoder
implicit val encoder19: Encoder[Geo] = deriveEncoder
implicit val encoder20: Encoder[User] = deriveEncoder
implicit val encoder21: Encoder[Data] = deriveEncoder
implicit val encoder22: Encoder[Segment] = deriveEncoder
implicit val encoder23: Encoder[Regs] = deriveEncoder
implicit val encoder24: Encoder[BidResponse] = deriveEncoder
implicit val encoder25: Encoder[SeatBid] = deriveEncoder
implicit val encoder26: Encoder[Bid] = deriveEncoder
implicit val encoder27: Encoder[NativeRequest] = deriveEncoder
implicit val encoder28: Encoder[Asset] = deriveEncoder
implicit val encoder29: Encoder[Title] = deriveEncoder
implicit val encoder30: Encoder[Image] = deriveEncoder
implicit val encoder31: Encoder[Data] = deriveEncoder
implicit val encoder32: Encoder[EventTrackers] = deriveEncoder
implicit val encoder33: Encoder[NativeResponse] = deriveEncoder
implicit val encoder34: Encoder[Link] = deriveEncoder
implicit val encoder35: Encoder[Asset] = deriveEncoder
implicit val encoder36: Encoder[Title] = deriveEncoder
implicit val encoder37: Encoder[Image] = deriveEncoder
implicit val encoder38: Encoder[Data] = deriveEncoder
implicit val encoder39: Encoder[Video] = deriveEncoder
implicit val encoder40: Encoder[EventTracker] = deriveEncoder

// val obj = json.as[BidRequest].right.get

val obj = BidRequest(
          id = "0c53b0a471674179860ac75ecc5ed473",
          distributionchannelOneof =  DistributionchannelOneof.Site(Site(id = Some("site1"), domain = Some("vpon.com"))),
          imp = Seq(
            Imp(
              id = "d17b076eeb964414a57955f24f583935",
              banner = Some(
                Banner(
                  w = Some(320),
                  h = Some(50),
                  id = Some("11111"),
                  pos = Some(AdPosition.ABOVE_THE_FOLD)
                )
              ),
              instl = Some(true),
              bidfloor = Some(1100000),
              bidfloorcur = Some("TWD")
          )),
          device = Some(
            Device(
              ua = Some(
                "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_4 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Mobile/11B554a"
              ),
              ip = Some("110.249.163.214"),
              os = Some("iPhone OS"),
              w = Some(1920),
              h = Some(1080),
              ppi = Some(100),
              pxratio = Some(2.4f),
              language = Some("zh"),
              ifa = Some("idfa"),
              dpidsha1 = Some("udidsha1"),
              dpidmd5 = Some("udidmd5")
            )
          ),
          user = Some(
            User(
              id = Some("123456"),
              buyeruid = Some("654321"),
              yob = Some(2015),
              gender = Some("M")
            )
          ),
          test = Some(false),
          at = Some(AuctionType.SECOND_PRICE),
          tmax = Some(3)
        
    )

println(s"result ${obj.asJson}")