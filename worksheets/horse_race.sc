import math._
import scala.util._

object HorseSelector {
  def apply(horses: Seq[Int]): Int = {
    horses
      .sorted
      .sliding(2, 1)
      .map({
        case Seq(a, b) => b - a
      }).min
  }
}

object Solution extends App {
  val horses = (0 to readInt - 1).map(_ => readInt)
  val solution = HorseSelector(horses)
  println(solution)
}

val horses = Seq(5, 8, 9)
println(HorseSelector(horses))