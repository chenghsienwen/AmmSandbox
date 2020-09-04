import protobuf.comm.{CreativeSspInfo, DeviceIdInfo, NativeContent}
import protobuf.comm.NativeContent.DeliveryType
// import $file.EnumPlus
sealed trait StartDelay {
  val openRtbValue: Option[Int]
  val criterionValue: String
}

object StartDelay {
  final case class MidRoll(seconds: Option[Int]) extends StartDelay {
    // it is generic midroll if "seconds" is none
    val openRtbValue: Option[Int] = Some(seconds.getOrElse(-1))
    val criterionValue: String    = "midroll"
  }
  final case object PreRoll extends StartDelay {
    val openRtbValue: Option[Int] = Some(0)
    val criterionValue: String    = "preroll"
  }
  final case object PostRoll extends StartDelay {
    val openRtbValue: Option[Int] = Some(-2)
    val criterionValue: String    = "postroll"
  }
  final case object UnknownRoll extends StartDelay {
    val openRtbValue: Option[Int] = None
    val criterionValue: String    = "unknown"
  }
}


object CreativePublishType extends scala.Enumeration {
  val TEXT              = Value(0, "text")
  val IMAGE             = Value(1, "image")
  val HTML              = Value(2, "html")
  val NATIVE            = Value(3, "native")
  val VIDEO             = Value(4, "video")
  val NATIVE_WITH_VIDEO = Value(5, "native with video")
  val UNKNOWN           = Value(99, "unknown")
}
object DealType extends scala.Enumeration {
  val CPM = Value(1, "cpm")
  val CPC = Value(2, "cpc")
}
/**
  * Follow OpenRTB
  * There are 9 and 10 at OpenRTB
  */
object NoBidReason extends scala.Enumeration {
  val Unknown           = Value(0, "unknown error")
  val TechnicalError    = Value(1, "technical error")
  val InvalidRequest    = Value(2, "invalid request")
  val WebSpider         = Value(3, "known web spider")
  val NonHumanTraffic   = Value(4, "non-human traffic")
  val ProxyIp           = Value(5, "cloud, data center, or proxy IP")
  val UnsupportedDevice = Value(6, "unsupported device")
  val BlockedPublisher  = Value(7, "blocked publisher or site")
  val UnmatchedUser     = Value(8, "unmatched user")
  val SkipBid     = Value(9, "skip bid actor")
  // val MetDailyReaderCap = Value(9, "daily reader cap met")
  // val MetDailyDomainCap = Value(10, "daily domain cap met")
}
sealed trait VideoPlacement {
  val openRtbValue: Option[Int]
  val criterionValue: String
}

object VideoPlacement {
  final case object Unknown extends VideoPlacement {
    val openRtbValue: Option[Int] = Some(-1)
    val criterionValue: String    = "unknown"
  }
  final case object InStream extends VideoPlacement {
    val openRtbValue: Option[Int] = Some(1)
    val criterionValue: String    = "instream"
  }
  final case object InBanner extends VideoPlacement {
    val openRtbValue: Option[Int] = Some(2)
    val criterionValue: String    = "inbanner"
  }
  final case object InArticle extends VideoPlacement {
    val openRtbValue: Option[Int] = Some(3)
    val criterionValue: String    = "inarticle"
  }
  final case object InFeed extends VideoPlacement {
    val openRtbValue: Option[Int] = Some(4)
    val criterionValue: String    = "infeed"
  }
  final case object Interstitial extends VideoPlacement {
    val openRtbValue: Option[Int] = Some(5)
    val criterionValue: String    = "interstitial"
  }
}
case class BannerObj(w: Option[Int] = None,
                     h: Option[Int] = None,
                     actualW: Option[Int] = None,
                     actualH: Option[Int] = None,
                     id: Option[String] = None,
                     pos: Option[Seq[Int]] = None,
                     battr: Option[Seq[Int]] = None,
                     btype: Option[Seq[Int]] = None,
                     mimes: Option[Seq[String]] = None,
                     topframe: Option[Int] = None,
                     expdir: Option[Seq[Int]] = None,
                     api: Option[Seq[Int]] = None,
                     preferredSize: Option[Dimension] = None,
                     baiduCrtType: Seq[Int] = Seq(),
                     ext: Option[Map[String, String]] = None)

case class Dimension(width: Int, height: Int)

case class VideoObj(mimes: Seq[String],
                    minduration: Int,
                    maxduration: Int,
                    linearity: Option[Int] = None, // 1 is linear, 2 is overlay
                    protocol: Option[Int] = None,
                    w: Option[Int] = None,
                    h: Option[Int] = None,
                    startdelay: StartDelay = StartDelay.UnknownRoll,
                    sequence: Option[Int] = None,
                    battr: Option[Seq[Int]] = None,
                    maxextended: Option[Int] = None,
                    minbitrate: Option[Int] = None,
                    maxbitrate: Option[Int] = None,
                    boxingallowed: Option[Int] = None,
                    playbackmethod: Option[Seq[Int]] = None,
                    delivery: Option[Seq[Int]] = None,
                    pos: Option[Int] = None,
                    companionad: Option[Seq[BannerObj]] = None,
                    api: Option[Seq[Int]] = None,
                    companiontype: Option[Seq[Int]] = None,
                    ext: Option[Map[String, String]] = None,
                    protocols: Option[Seq[Int]] = None,
                    skip: Option[Int] = None,
                    skipmin: Option[Int] = None,
                    skipafter: Option[Int] = None,
                    clickable: Option[Boolean] = None, // doubleclick
                    adslotId: Option[String] = None, // it is used to generate placement id
                    skippableMaxAdDuration: Option[Int] = None, // it is used by doubleclick now
                    maxFileSize: Option[Long] = None, //In Bytes, used by XTrader for now
                    descUrl: Option[String] = None,
                    placement: Option[VideoPlacement] = None)
/**
  * OpenRTB - Object: Bid
  * @param id
  * @param impid
  * @param price
  * @param adid
  * @param nurl OpenRTB, Win notice URL. Vpon also uses as billing notice URL
  * @param adm
  * @param adomain
  * @param iurl
  * @param cid
  * @param crid
  * @param platformid Non-OpenRTB
  * @param attr
  * @param cpt Non-OpenRTB, [[CreativePublishType]]
  * @param curl Non-OpenRTB
  * @param cturl Non-OpenRTB
  * @param w
  * @param h
  * @param destUrlForAudition Non-OpenRTB
  * @param sequenceId
  * @param advertiserId
  * @param impUrl
  * @param creVer
  * @param baiducat
  * @param baiduCrtType
  * @param billingId
  * @param clickId
  * @param density
  * @param mopubcat
  * @param mopubAppUrl
  * @param tanxCats
  * @param tanxAdvertiserIds
  * @param advertiserUrl
  * @param wseat
  * @param actualUsdPrice
  * @param nativeAdm
  * @param secure
  * @param language
  * @param bidcurrency
  * @param s2sNurl
  * @param sspClkTrk
  * @param dealid
  * @param advertiserIndustry
  * @param advertiserSubIndustry
  * @param clickTracking
  * @param allowStyle
  * @param nexCrid
  * @param nativeDeliveryType
  * @param iabCat OpenRTB's cat
  * @param dealType
  * @param pptvCrid
  * @param videoAdm
  * @param impressionContext
  * @param sspInfo
  * @param macroReplacer
  * @param impressionTrackingUrl
  */
case class BidObj(
    id: String,
    impid: String,
    price: BigDecimal,
    adid: Option[String] = None,
    nurl: Option[String] = None,
    adm: Option[String] = None,
    adomain: Option[Seq[String]] = None,
    iurl: Option[String] = None,
    cid: Option[String] = None,
    crid: Option[String] = None,
    platformid: Option[String] = None,
    attr: Option[Seq[Int]] = None,
    cpt: CreativePublishType.Value = CreativePublishType.HTML,
    curl: String = "", // defined in AMAX spec
    cturl: Option[Seq[String]] = None, // defined in AMAX spec
    w: Option[Int] = None,
    h: Option[Int] = None,
    destUrlForAudition: Option[String] = None,
    sequenceId: Option[String] = None, //baidu
    advertiserId: Option[String] = None, //baidu
    impUrl: Option[String] = None, //baidu
    creVer: Option[Int] = None, //baidu
    baiducat: Option[Int] = None, //baidu
    baiduCrtType: Option[Seq[Int]] = None, //baidu
    billingId: Option[Long] = None, //DoubleClick
    clickId: Option[String] = None,
    density: Option[Int] = None,
    mopubcat: Option[Seq[String]] = None, //mopub
    mopubAppUrl: Option[String] = None, //mopub
    tanxCats: Seq[Int] = Seq.empty[Int], //tanx
    tanxAdvertiserIds: Seq[Int] = Seq.empty[Int], //tanx
    advertiserUrl: Option[String] = None,
    wseat: Option[Seq[String]] = None,
    actualUsdPrice: Option[BigDecimal] = None, // monitoring
    nativeAdm: Option[NativeAdMarkup] = None,
    secure: Option[Int] = None,
    language: Option[Int] = None,
    bidcurrency: Option[String] = None,
    s2sNurl: Option[Int] = None, //bidswitch
    sspClkTrk: Option[Int] = None, //bidswitch
    dealid: Option[String] = None,
    advertiserIndustry: Option[String] = None, // Nex
    advertiserSubIndustry: Option[String] = None,
    clickTracking: String = "",
    allowStyle: Option[Seq[Int]] = None, // Nex
    nexCrid: Option[String] = None, // Nex
    nativeDeliveryType: Option[DeliveryType] = None,
    iabCat: Option[Seq[String]] = None,
    dealType: DealType.Value = DealType.CPM,
    pptvCrid: Option[String] = None,
    videoAdm: Option[VideoAdMarkup] = None,
    impressionContext: Option[Any] = None, //Generic context forwarded from bidReq.imp.impressionContext
    sspInfo: Option[CreativeSspInfo] = None, // put ssp specific information and help to compose final bid response
    macroReplacer: Option[(String => String)] = None,
    impressionTrackingUrl: Seq[String] = Seq.empty,
    deviceIdInfo: Option[DeviceIdInfo] = None,
    lurl: Option[String] = None
)

case class SeatBidObj(bid: Seq[BidObj],
                      seat: Option[String] = None,
                      group: Option[Int] = None,
                      ext: Option[Map[String, String]] = None)

/*
  id          (string, required)   ID of the bid request (i.e., bid-request.id).
  seatbid     (object array, required)  Set of seat-bids (see “Object: Seatbid”).
  bidid       (string)  Unique ID of the bid response for Bidder tracking purposes.
  cur         (string, default=”USD”)  Bid currency per ISO-4217-alpha.
  customdata  (string)  Unformatted cookie-safe data to be set in the Exchange’s cookie (see “user.customdata”).1
  nbr         (integer)  Reason for not bidding, where 0 = unknown error, 1 = technical error, 2 = invalid request,
                          3 = known web spider, 4 = suspected Non-Human Traffic, 5 = cloud, data center, or proxy IP,
                          6 = unsupported device, 7 = blocked publisher or site, 8 = unmatched user.
  ext         (object)  Exchange specific extensions.
 */

case class BidResponseObj(id: String,
                          seatbid: Seq[SeatBidObj],
                          bidid: Option[String] = None,
                          cur: Option[String] = None,
                          customdata: Option[String] = None,
                          nbr: Option[NoBidReason.Value] = None,
                          ext: Option[Map[String, String]] = None) {
  def isNoBid: Boolean = !seatbid.exists(_.bid.exists(_.price > 0))
}

case class NativeAdMarkup(native: NativeAdMarkup.NativeObj)

object NativeAdMarkup {

  /**
    * Subset of OpenRTB Native Markup Response Object
    * @param ver
    * @param assets
    * @param link
    * @param imptrackers
    * @param jstracker
    * @param ext
    * @param deliveryType Non-OpenRTB
    */
  case class NativeObj(ver: Option[String] = None,
                       assets: Seq[AssetObj],
                       link: LinkObj,
                       imptrackers: Option[Seq[String]],
                       jstracker: Option[String],
                       ext: Option[Map[String, String]] = None,
                       deliveryType: NativeContent.DeliveryType)

  case class AssetObj(id: Int,
                      required: Option[Boolean],
                      title: Option[TitleObj],
                      img: Option[ImgObj],
                      data: Option[DataObj],
                      link: Option[LinkObj] = None, // use NativeObj's link by default
                      video: Option[VideoObj] = None,
                      ext: Option[Map[String, String]] = None)

  case class TitleObj(text: String, ext: Option[Map[String, String]] = None)
  case class ImgObj(url: String,
                    w: Option[Int],
                    h: Option[Int],
                    ext: Option[Map[String, String]] = None,
                    mimes: Option[String] = None)
  case class DataObj(label: Option[String] = None, value: String, ext: Option[Map[String, String]] = None)
  case class LinkObj(url: String,
                     clicktrackers: Option[Seq[String]],
                     fallback: Option[String] = None,
                     ext: Option[Map[String, String]] = None)
}

case class VideoAdMarkup(rawVast: Option[String] = None,
                         urlToGetVast: Option[String] = None,
                         videoUrl: Option[String] = None,
                         impressionTrackings: Trackings,
                         clickTrackings: Trackings)

case class Trackings(selfTracking: String, thirdPartyTrackings: Seq[String]) {

  def getAllTrackings = selfTracking +: thirdPartyTrackings

  def replaceMacrosBy(funcToReplaceMacros: String => String): Trackings =
    Trackings(
      selfTracking = funcToReplaceMacros(selfTracking),
      thirdPartyTrackings = thirdPartyTrackings.map(funcToReplaceMacros)
    )

}

object BidResponse {
  val NO_BID_TECHNICAL = BidResponseObj("", Seq(), nbr = Some(NoBidReason.TechnicalError))
}
