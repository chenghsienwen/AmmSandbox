interp.load.ivy("ru.yandex.clickhouse" % "clickhouse-jdbc" % "0.3.1")
@
import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions
import ru.yandex.clickhouse._
import ru.yandex.clickhouse.settings.ClickHouseProperties
import ru.yandex.clickhouse.settings.ClickHouseQueryParam
val url = "jdbc:clickhouse://localhost:8123/default"
val properties = new ClickHouseProperties()
// set connection options - see more defined in ClickHouseConnectionSettings
properties.setClientName("Agent #1")
properties.setSessionId("default-session-id")
properties.setUser("default")
properties.setPassword("")

val  dataSource = new ClickHouseDataSource(url, properties)
val sql = "select * from visits"
val additionalDBParams = Map(ClickHouseQueryParam.SESSION_ID -> "new-session-id")
// set request options, which will override the default ones in ClickHouseProperties


val conn = dataSource.getConnection()
val stmt = conn.createStatement()
val rs = stmt.executeQuery(sql, additionalDBParams.asJava)

println(rs)