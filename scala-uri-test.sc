interp.load.ivy("io.lemonlabs" %% "scala-uri" % "2.3.1")

@
import io.lemonlabs.uri.Url
import io.lemonlabs.uri._
import java.net.URLEncoder
val url = Url.parse("https://apies.baidu.com/v1/viewconfig/appid2name/get")

val params = Map("aa" -> "v1", "bb" -> "v2", "cc" -> 2.toString, "dd" -> URLEncoder.encode("https://www.vpon.com/", "UTF-8"))
val url2 = url.addParams(params).toStringRaw

println(url.hostOption)

println(url2)

val url3 = "https://stg-c-dsp.vpon.com/beacon?t=1&cid=d.oJbar5bIvoItocSPIgKBnJPT49HLtaYYga609t-kzoDPRxxSRURBQ1RFRCBGT1IgUFJJVkFDWSBSRUFTT05TBGMAXUCJIASNmTYBsdTI.2504&did=524544414354454420464F52205052495641435920524541534F4E53&conv=&rurl=https%3A%2F%2Fwww.vpon.com%2F&rand="

val url4 = Url.parse(url3)

val result = url4 match {
    case AbsoluteUrl(protocol, authority, path, queryString, _) => authority.toStringRaw
     case _ => url4.apexDomain.getOrElse("")
}
println(result)