package exercices.s3FunctionalTypes

import scala.io.Source
import scala.util.{Failure, Success, Try}

object ReadFile {
  /**
    * Complète les quatre fonctions avec des ??? pour faire passer les tests
    */
  type Header = String

  // lit un fichier et retourne son contenu ligne à ligne (Seq[String])
  def readFile(path: String): Try[Seq[String]] =
    Try(Source.fromFile(path).getLines.toSeq)

  // parse le csv, extrait les headers et combine les avec les données
  def readLines(lines: Seq[String]): Seq[Map[Header, String]] = lines match {
    case firstLine +: others =>
      val headers = parseLine(firstLine)
      others.map { line => addHeaders(parseLine(line), headers) }
    case _ => Seq()
  }

  private def parseLine(line: String): Seq[String] =
    line.split(",")

  private def addHeaders(line: Seq[String], headers: Seq[String]): Map[Header, String] =
    headers.zip(line).toMap

  case class User(id: Int, firstName: String, lastName: String, email: String, gender: String, ip: Option[String])

  // transforme les données d'une ligne de csv en case class User
  def formatLine(line: Map[Header, String]): Try[User] =
    Try(User(
      id = line("id").toInt,
      firstName = line("first_name"),
      lastName = line("last_name"),
      email = line("email"),
      gender = line("gender"),
      ip = line.get("ip_address").filterNot(_.isEmpty)))

  // combine les méthodes précédentes pour obtenir les résultats et erreurs
  def loadData(path: String): (Seq[User], Seq[(Int, Throwable)]) = {
    val (success, errors) =
      readFile(path)
        .map(readLines)
        .map(_.map(formatLine))
        .getOrElse(Seq())
        .zipWithIndex
        .partition(_._1.isSuccess)
    (success.collect { case (Success(u), _) => u }, errors.collect { case (Failure(e), i) => (i + 1, e) })
  }
}
