
//NOTE: only work on repl, script still fail
import $ivy.`org.typelevel::cats-core:2.0.0`
//ref: https://github.com/com-lihaoyi/Ammonite/issues/534
//ref: https://gist.github.com/MateuszKubuszok/a80503b28f289f08f2f6c6c70871e8d3#log-implicits
//ref: https://www.scala-lang.org/api/2.12.4/scala-compiler/scala/tools/nsc/Settings.html
interp.configureCompiler(_.settings.Ydelambdafy.value ="inline")
@

import cats.data.Reader
import cats.Eq
import cats.instances.all._
import cats.syntax.eq._ //for eq

final case class Db(
    usernames: Map[Int, String],
    passwords: Map[String, String]
)

type DbReader[A] = Reader[Db, A]

def findUsername(userId: Int): DbReader[Option[String]] = {
    Reader(db => db.usernames.get(userId))
}

def checkPassword(usernameOpt: Option[String], password: String): DbReader[Boolean] = {
    Reader(db => usernameOpt.map(username => db.passwords.get(username).exists(i => i === password))
    .getOrElse(false)
    )
}

def checkLogin(userId: Int, password: String): DbReader[Boolean] = {
    for {
        nameOpt <- findUsername(userId)
        isValid <- checkPassword(nameOpt, password)
    } yield {
        isValid
    }
}

val users = Map(
    1 -> "dade",
    2 -> "kate",
    3 -> "margo"
)
val passwords = Map(
"dade" -> "zerocool",
"kate" -> "acidburn",
"margo" -> "secret"
)
val db = Db(users, passwords)
val res1 = checkLogin(1, "zerocool").run(db)
// res7: cats.package.Id[Boolean] = true
val res2 = checkLogin(4, "davinci").run(db)
// res8: cats.package.Id[Boolean] = false

println(s"res1: ${res1}, res2: ${res2}")






