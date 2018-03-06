package exercices.s1Basics

object FizzBuzz {
  /**
    * Code ici la fonction fizzBuzz qui prends un entier en paramètre et renvoit une String en résultat.
    * Le résultat devra être :
    *   - Fizz si le nombre en paramètre est divisible par 3
    *   - Buzz si le nombre en paramètre est divisible par 5
    *   - FizzBuzz si le nombre en paramètre est divisible par 3 et par 5
    *   - le nombre sous forme de String sinon
    */

  def apply(i: Int): String = {
    (i % 3 == 0, i % 5 == 0) match {
      case (true, true) => "FizzBuzz"
      case (true, false) => "Fizz"
      case (false, true) => "Buzz"
      case _ => i.toString
    }
  }

  def main(args: Array[String]): Unit = {
    var i = args(0).toInt
    println(apply(i))
  }
}
