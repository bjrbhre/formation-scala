// https://www.codingame.com/ide/puzzle/defibrillators

import math._
import scala.util._

case class Location(lat: Double, lng: Double)

case class Defibrilator(id: Int, name: String, address: String, phone: String, location: Location)
// companion object
object Defibrilator {
  def parseLine(line: String): Defib = ???
}

object Haversine {
  val R = 6372.8  //radius in km

  def haversine(lat1:Double, lon1:Double, lat2:Double, lon2:Double)={
    val dLat=(lat2 - lat1).toRadians
    val dLon=(lon2 - lon1).toRadians

    val a = pow(sin(dLat/2),2) + pow(sin(dLon/2),2) * cos(lat1.toRadians) * cos(lat2.toRadians)
    val c = 2 * asin(sqrt(a))
    R * c
  }
}

object Solution extends App {
  def toCoordinate(s: String): Double = {
    s.replace(',', '.').toDouble
  }

  def toDefibrilator(s: String): Defibrilator = {
    // case matching to return Try[Defibrilator]
    // use for comprehension to type check int / doubles

    s.split(';').match {
      Array(id, name, address, phone, lat, lng) =>
        val debibTry = for {
          intIt Int <- Try(id.toInt),
          latDouble <- toCoordiante(lat),
          lngDouble <- toCoordiante(lng),
        } yield {
          Defibrilator(
            id.toInt,
            name,
            address,
            phone,
            Location(toCoordinate(lat), toCoordinate(lng))
      )
    )
        }
    }
    //val Array(id, name, address, phone, lat, lng) = s.split(';')
    Defibrilator(
      id.toInt,
      name,
      address,
      phone,
      Location(toCoordinate(lat), toCoordinate(lng))
    )
  }

  def distance(from: Location, to: Location): Double = {
    Haversine.haversine(from.lat, from.lng, to.lat, to.lng)
  }

  val userLocation: Location = Location(
    toCoordinate(readLine),
    toCoordinate(readLine)
  )

  val defibrilators = (0 until readInt)
    .map(_ => toDefibrilator(readLine))

  // Use minBy
  val minDistance = defibrilators.map(d => distance(userLocation, d.location)).min
  val nearest = defibrilators.find(d => distance(userLocation, d.location) == minDistance).get
  println(nearest.name)
}