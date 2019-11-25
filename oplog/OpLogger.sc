import $ivy.`com.outr::perfolation:1.0.4`
import $ivy.`org.typelevel::mouse:0.23`
import $ivy.`org.typelevel::cats-effect:2.0.0`
import $ivy.`io.chrisdavenport::log4cats-slf4j:1.0.1`
import $ivy.`io.circe::circe-parser:0.12.3`

import cats.Show
import cats.syntax.apply._
import cats.effect.Sync
// import $file.PipeOperator
import $file.OperationLog
import OperationLog._
import io.chrisdavenport.log4cats.{Logger, SelfAwareStructuredLogger}
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger

trait OpLogger {
  implicit private def unsafeLogger[F[_]: Sync]: SelfAwareStructuredLogger[F] = Slf4jLogger.getLogger[F]

  def logWithNoError[F[_]: Sync, D](l: NoErrorOperationLog[D])(implicit ds: Show[D]): F[String] =
    l.toJsonString.|> { s =>
      Logger[F].info(s) *> Sync[F].delay(s)
    }

  def logWithError[F[_]: Sync, E, D](l: WithErrorOperationLog[E, D])(implicit ds: Show[D], es: Show[E]): F[String] =
    l.toJsonString.|> { s =>
      Logger[F].error(s) *> Sync[F].delay(s)
    }
}
