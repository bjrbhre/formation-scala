package exercices.s3FunctionalTypes

import scala.io.Source
import scala.util.{Failure, Success, Try}

object ReadFile {
  /**
    * Complète les quatre fonctions avec des ??? pour faire passer les tests
    */
  type Header = String

  // lit un fichier et retourne son contenu ligne à ligne (Seq[String])
  def readFile(path: String): Try[Seq[String]] = {
    Try(Source.fromFile(path).getLines.toSeq)
  }

  // parse le csv, extrait les headers et combine les avec les données
  def parseFile(lines: Seq[String]): Seq[Map[Header, String]] = {
    val sep = ','
    lines match {
      case Seq(head, tail @ _ *) => {
        val headers = head.split(sep)
        tail.map { line =>
          headers.zip(line.split(sep)).toMap
        }
      }
      case _ => Seq()
    }
  }

  case class User(id: Int, firstName: String, lastName: String, email: String, gender: String, ip: Option[String])

  // transforme les données d'une ligne de csv en case class User
  def formatLine(line: Map[Header, String]): Try[User] = {
    Try {
      val id: Int = line("id").toInt
      val firstName: String = line("first_name")
      val lastName: String = line("last_name")
      val email: String = line("email")
      val gender: String = line("gender")
      //val ip: Option[String] = line.get("ip_address").filter{_ != ""}
      val ip: Option[String] = line.get("ip_address") match {
        case Some("") => None
        case x => x
      }
      User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        gender = gender,
        ip = ip
      )
    }
  }

  // combine les méthodes précédentes pour obtenir les résultats et erreurs
  def loadData(path: String): Try[(Seq[User], Seq[(Int, Throwable)])] = {
    readFile(path).map { lines =>
      val users = parseFile(lines).map(formatLine)
      val success = users.collect { case Success(user) => user }
      val failures = users.zipWithIndex.collect { case (Failure(e), i) => (i + 1, e) }
      (success, failures)
    }
  }
}
