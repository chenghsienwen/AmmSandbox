import coursierapi.{MavenRepository}

interp.repositories() ++= Seq(
  MavenRepository
    .of("http://nexus.vpon.com/content/repositories/releases")
)
interp.load.ivy("com.quadas"                 %% "dsp-agent-model"            % "1.0.2+191-52ccf765")
interp.load.ivy("com.quadas"                %% "dsp-protobuf-scalapb"     % "184")
interp.load.ivy("com.quadas"                %% "rapidstring"              % "0.1.3.2")

@
import $ivy.`com.typesafe.akka::akka-http:10.1.7`
import $ivy.`com.typesafe.akka::akka-stream:2.5.21`
import $ivy.`com.lihaoyi::requests:0.2.0`
import akka.http.scaladsl._
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.{ContentType, HttpEntity}
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import scala.concurrent.Await
import scala.concurrent.duration._
import $file.model.VponBidResponse
import protobuf.comm.doubleclick.BidResponse.Ad
import protobuf.comm.doubleclick.{BidRequest, BidResponse}
import protobuf.comm.{Cookie, CornerMark, CreativeContent, RawDeviceIdInfo}
import com.quadas.dsp.agent.model.{ClickId, UserIdType}
import protobuf.comm.doubleclick.BidRequest.AdSlot.MatchingAdData.DirectDeal.DealType.UNKNOWN_DEAL_TYPE
import protobuf.comm.doubleclick.BidRequest.AuctionType.FIXED_PRICE
import protobuf.comm.doubleclick.BidRequest.AdSlot.MatchingAdData.DirectDeal.{DealType => GoogleDealType}
import scala.util.{Try, Success, Failure}
implicit val system = akka.actor.ActorSystem("MySystem")
implicit val materializer = akka.stream.ActorMaterializer()
implicit val executionContext = system.dispatcher

object RichMediaCapabilityType extends Enumeration {
  type RichMediaCapabilityType = Value
  val RichMediaCapabilityFlash  = Value(34)
  val RichMediaCapabilityHTML5  = Value(39)
  val RichMediaCapabilitySSL    = Value(47)
  val RichMediaCapabilityNonSSL = Value(48)
}
implicit val BidResponseMarshaller: ToEntityMarshaller[BidResponse] =
    Marshaller.oneOf(
      Marshaller.withFixedContentType(`application/octet-stream`) { obj =>
        HttpEntity(`application/octet-stream`, obj.toByteArray)
      }
    )
val oneMillion = 1000000.0
implicit class DoubleClickBidResponseConverter(val bidResponse: VponBidResponse.BidResponseObj) extends AnyVal {

    import collection.immutable._

    import protobuf.comm.doubleclick._
def toDoubleClickBidResponseObj(startTimeInMillis: Long): protobuf.comm.doubleclick.BidResponse = {
      //Assume there will be only one seatbid for now
      val groupedBid = bidResponse.seatbid.head.bid.groupBy(bidObj => (bidObj.id, bidObj.impid))
      val ad = groupedBid.map { impIdAndBidObjs =>
        val ((_, bidObjImpId), bidObjs) = impIdAndBidObjs

        val bidObjWithHighestPrice = bidObjs.sortBy(_.price).last

        val adslots = Ad.AdSlot(
          id = bidObjImpId.toInt,
          billingId = bidObjWithHighestPrice.billingId,
          maxCpmMicros = (bidObjWithHighestPrice.price * oneMillion).toLong, //CPM=1.29 => maxCpmMicros=1290000
          dealId = Try(bidObjs.map(_.dealid).headOption.flatten.map(_.toLong)) match {
            case Success(value) => value
            case Failure(_) => None
          }
        )

        BidResponse.Ad(
          buyerCreativeId =
            Some(s"""${bidObjWithHighestPrice.platformid.getOrElse("")}_${bidObjWithHighestPrice.crid
              .getOrElse("")}_${bidObjWithHighestPrice.creVer.getOrElse("")}"""),
          width = bidObjWithHighestPrice.w,
          height = bidObjWithHighestPrice.h,
          clickThroughUrl = bidObjWithHighestPrice.destUrlForAudition.map(Seq(_)).getOrElse(Seq.empty),
          htmlSnippet = bidObjWithHighestPrice.adm, //deprecated on video
          videoUrl = bidObjWithHighestPrice.videoAdm.flatMap(_.urlToGetVast),
          attribute = Seq(RichMediaCapabilityType.RichMediaCapabilitySSL.id),
          adslot = Seq(adslots),
          impressionTrackingUrl = bidObjWithHighestPrice.impressionTrackingUrl //deprecated on video
        )
      }
      val bidResp =
        protobuf.comm.doubleclick.BidResponse(ad = ad.toList, processingTimeMs = Some((System.currentTimeMillis() - startTimeInMillis).toInt))
      println("[doubleclick] toDoubleClickResponse: {}", bidResp)
      bidResp
  }
}
val bidResp1 = VponBidResponse.BidResponseObj(
        id = "bidresp1",
        seatbid = Seq(
          VponBidResponse.SeatBidObj(
            bid = Seq(
              VponBidResponse.BidObj(
                id = "bid1",
                billingId = Some(10001L),
                impid = "1",
                adid = Some("3"),
                w = Some(320),
                h = Some(50),
                platformid = Some(""),
                crid = Some("1"),
                creVer = Some(0),
                price = BigDecimal(1),
                adm = Some("test1"),
                curl = "http://www.vpon.com",
                destUrlForAudition = Some("http://www.vpon.com/dd"),
                dealid = Some("111")
              ),
              VponBidResponse.BidObj(
                id = "bid1",
                billingId = Some(10001L),
                impid = "1",
                adid = Some("4"),
                w = Some(720),
                h = Some(90),
                platformid = Some(""),
                crid = Some("2"),
                creVer = Some(0),
                price = BigDecimal(2),
                adm = Some("test2"),
                curl = "http://www.vpon.com",
                destUrlForAudition = Some("http://www.vpon.com/dd"),
                dealid = Some("111")
              ),
              VponBidResponse.BidObj(
                id = "bid2",
                billingId = Some(10001L),
                impid = "1",
                adid = Some("3"),
                w = Some(320),
                h = Some(50),
                platformid = Some(""),
                crid = Some("1"),
                creVer = Some(0),
                price = BigDecimal(1),
                adm = Some("test1"),
                curl = "http://www.vpon.com",
                destUrlForAudition = Some("http://www.vpon.com/dd"),
                dealid = Some("111")
              )
            )
          )
        )
      )

      val expectResp1 = BidResponse(
        ad = Vector(
          Ad(
            buyerCreativeId = Some("_1_0"),
            width = Some(320),
            height = Some(50),
            htmlSnippet = Some("test1"),
            clickThroughUrl = Seq("http://www.vpon.com/dd"),
            attribute = Seq(RichMediaCapabilityType.RichMediaCapabilitySSL.id),
            adslot = Seq(Ad.AdSlot(id = 1, billingId = Some(10001L), maxCpmMicros = 1000000, dealId = Some(111L)))
          ),
          Ad(
            buyerCreativeId = Some("_2_0"),
            width = Some(720),
            height = Some(90),
            htmlSnippet = Some("test2"),
            clickThroughUrl = Seq("http://www.vpon.com/dd"),
            attribute = Seq(RichMediaCapabilityType.RichMediaCapabilitySSL.id),
            adslot = Seq(Ad.AdSlot(id = 1, billingId = Some(10001L), maxCpmMicros = 2000000, dealId = Some(111L)))
          )
        )
      )

val messageEntity = Marshal[BidResponse](
         bidResp1.toDoubleClickBidResponseObj(0L)
       ).to[MessageEntity]

val bidRes = for {
        trans <- messageEntity
        res   <- Unmarshaller.byteArrayUnmarshaller(trans)
      } yield {
        res
      }
val res = Await.result(bidRes, 10.seconds)
println(BidResponse.parseFrom(res))
