import $ivy.`org.typelevel::cats-core:2.0.0`
import $ivy.`org.typelevel::cats-effect:2.0.0`
import $ivy.`io.catbird::catbird-effect:19.10.0`
import $file.oplog.ExceptionError
import $file.oplog.OpLogger
import $file.oplog.OperationLog, OperationLog._
import cats.Show
import cats.effect.IO
import cats.syntax.apply._
import ExceptionError._
import OpLogger._
import perfolation._
import io.catbird.util.Rerunnable
import cats.syntax.eq._

final case class SampleData(accountId: String, txId: String, response: String)

val sampleNoErrorLog = NoErrorOperationLog[SampleData](
  team = Team("architect"),
  service = Service("Sample Service"),
  msgLevel = PriorityLevel.P0,
  data = Data[SampleData](SampleData(accountId = "45909f22-f6fe-11e9-a3f5-784f436a2b87",
                                      txId = "70915c16-f6fe-11e9-9598-784f436a2b87",
                                      response = "login succeeded")))

implicit val sdShow: Show[SampleData] =
  Show.show(data =>
    p"""{ "accountId": "${data.accountId}",
        |     "txId": "${data.txId}",
        |     "response": "${data.response}"
        | }""".stripMargin)

object SampleApp extends OpLogger {
  def writeLog(d: NoErrorOperationLog[SampleData]): String =
    (IO.delay(println("Op Log ==> ")) *> logWithNoError[IO, SampleData](d)).unsafeRunSync
}

println(SampleApp.writeLog(sampleNoErrorLog))