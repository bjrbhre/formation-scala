package exercices.s4FunctionalProgramming

import java.sql.Connection

import scala.util.{Random, Try}

object GetConnectionRefactoring {
  /**
    * Spec: get a connection from an url
    *
    * Problems:
    *   - do not open more than one successful connection
    *   - use functional style
    */
  def connect(urls: Seq[String]): Option[Connection] = {
    var connection: Option[Connection] = None
    Random.shuffle(urls).find { url =>
      connection = getConnection(url).toOption
      connection.isDefined
    }.flatMap(_ => connection)
  }

  def connectLazy(urls: Seq[String]): Option[Connection] = {
    Random.shuffle(urls).toStream
      .flatMap(url => getConnection(url).toOption)
      .headOption
  }

  private def getConnection(url: String): Try[Connection] = {
    ???
  }
}
