import $ivy.`com.google.doubleclick:doubleclick-core:0.8.0-beta2`
import $ivy.`com.google.guava:guava:18.0`
import com.google.common.io.BaseEncoding
import javax.crypto.spec.SecretKeySpec
import com.google.doubleclick.crypto.{DoubleClickCrypto => defaultDoubleClickCrypto}
import com.google.doubleclick.crypto.DoubleClickCrypto.{Hyperlocal, Idfa}

val eKey = "00aa81180011452897f71c9f0011452897f761ba0011452897f76be662d3d67b"
val iKey = "00aa81180011452897f7f6310011452897f804490011452897f80d8f63d0fbdb"
val KEYS = new defaultDoubleClickCrypto.Keys(
    new SecretKeySpec(BaseEncoding.base64().decode(eKey), "HmacSHA1"),
    new SecretKeySpec(BaseEncoding.base64().decode(iKey), "HmacSHA1")
  )
val idfaCryptor        = new defaultDoubleClickCrypto.Idfa(KEYS)
val dcCryptoHyperlocal = new defaultDoubleClickCrypto.Hyperlocal(KEYS)

val cryptor     = new defaultDoubleClickCrypto.Price(KEYS)

val testPrice = 100

val encodePrice = cryptor.encryptPriceMicros(testPrice, Array.empty[Byte])

val decodePrice = cryptor.decodePriceMicros(BaseEncoding.base64().encode(encodePrice))

println(s" decodePrice $decodePrice")



