import $ivy.`com.outr::perfolation:1.0.4`
import $ivy.`org.typelevel::mouse:0.23`
import $ivy.`org.typelevel::cats-effect:2.0.0`
import $ivy.`io.chrisdavenport::log4cats-slf4j:1.0.1`
import $ivy.`io.circe::circe-parser:0.12.3`
import $ivy.`com.twitter::util-core:19.11.0`
import $ivy.`com.beachape::enumeratum:1.5.13`
import cats.Show
import OperationLog.{Error => OpError, _}
import com.twitter.util.Time
import enumeratum._
import io.circe.parser._
import mouse.option._
import org.slf4j.MDC
import perfolation._

import scala.collection.immutable
import scala.util.Try

final case class NoErrorOperationLog[D](
    team: Team,
    service: Service,
    timestamp: Long         = Time.now.inMillis,
    traceId: Option[String] = Try(MDC.get("traceId")).toOption,
    msgLevel: PriorityLevel,
    data: Data[D]
) {

  def toJsonString(implicit ds: Show[D]): String =
    traceId.cata(
      tId => noSpaceJson(p"""
                            |{
                            |  "team": "${team.name}",
                            |  "service": "${service.name}",
                            |  "timestamp": $timestamp,
                            |  "traceId": "$tId",
                            |  "msgLevel": "${msgLevel.entryName}",
                            |  "data": ${ds.show(data.d)}
                            |}
                            |""".stripMargin),
      noSpaceJson(p"""
                     |{
                     |  "team": "${team.name}",
                     |  "service": "${service.name}",
                     |  "timestamp": $timestamp,
                     |  "msgLevel": "${msgLevel.entryName}",
                     |  "data": ${ds.show(data.d)}
                     |}
                     |""".stripMargin)
    )

  private val noSpaceJson: String => String = s => parse(s).fold(_.message, _.noSpaces)
}

final case class WithErrorOperationLog[E, D](
    team: Team,
    service: Service,
    timestamp: Long         = Time.now.inMillis,
    traceId: Option[String] = Try(MDC.get("traceId")).toOption,
    msgLevel: PriorityLevel,
    data: Data[D],
    error: OpError[E]
) {

  def toJsonString(implicit ds: Show[D], es: Show[E]): String =
    traceId.cata(
      tId => noSpaceJson(p"""
                            |{
                            |  "team": "${team.name}",
                            |  "service": "${service.name}",
                            |  "timestamp": $timestamp,
                            |  "traceId": "$tId",
                            |  "msgLevel": "${msgLevel.entryName}",
                            |  "data": ${ds.show(data.d)},
                            |  "error": ${es.show(error.e)}
                            |}
                            |""".stripMargin),
      noSpaceJson(p"""
                     |{
                     |  "team": "${team.name}",
                     |  "service": "${service.name}",
                     |  "timestamp": $timestamp,
                     |  "msgLevel": "${msgLevel.entryName}",
                     |  "data": ${ds.show(data.d)},
                     |  "error": ${es.show(error.e)}
                     |}
                     |""".stripMargin)
    )

  private val noSpaceJson: String => String = s => parse(s).fold(_.message, _.noSpaces)
}

// object OperationLog {
  final case class Team(name: String)    extends AnyVal
  final case class Service(name: String) extends AnyVal
  final case class Action(name: String)  extends AnyVal

  final case class Data[D](d: D)
  final case class Error[E](e: E)

  sealed trait PriorityLevel extends EnumEntry

  object PriorityLevel extends Enum[PriorityLevel] {
    val values: immutable.IndexedSeq[PriorityLevel] = findValues

    case object P0 extends PriorityLevel
    case object P1 extends PriorityLevel
    case object P2 extends PriorityLevel
    case object P3 extends PriorityLevel
  }
// }
