interp.load.ivy("io.lemonlabs" %% "scala-uri" % "2.3.1")

@
import io.lemonlabs.uri.Url
import java.net.URLEncoder
val url = Url.parse("https://apies.baidu.com/v1/viewconfig/appid2name/get")

val params = Map("aa" -> "v1", "bb" -> "v2", "cc" -> 2.toString, "dd" -> URLEncoder.encode("https://www.vpon.com/", "UTF-8"))
val url2 = url.addParams(params).toStringRaw

println(url.hostOption)

println(url2)