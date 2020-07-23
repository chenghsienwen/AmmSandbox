import $ivy.{
    `com.github.3tty0n::jwt-scala:1.3.0`
}

import com.github._3tty0n.jwt._
import play.api.libs.json.Json

val payload = Json.obj("name" -> "Ahmed", "email" -> "ahmed@gmail.com")
val jwtEncode = JWT.encode("secret-1234", payload)

val jwtDecode = JWT.decode(jwtEncode, Some("secret-1234"))

println(s"jwtEncode: $jwtEncode, jwtDecode: $jwtDecode")