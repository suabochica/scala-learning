package rockthejvm

object PatternMatching extends App {
  // Switch Expression
  val anInteger = 55
  val order = anInteger match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => anInteger + "th"
  }

  // Pattern matching is an expression!

  // Case class decomposition
  case class Person(name: String, age: Int)

  val jim = Person("Jim", 38)

  val personGreeting = jim match {
    case Person (name, age) => s"Hi, my name is $name and I am $age years old."
    case _ => "Something else"
  }

  // Deconstructing tuples
  val aTuple = ("Bon Jovi", "Rock")
  val bandDescription = aTuple match {
    case (band, genre) => s"$band belongs to the genre $genre"
    case _ => "I don't know what are you talking about"
  }

  // Decomposing list
  val aList = List(1, 2, 3)
  val listDescription = aList match {
    case List (_, 2, _) => "List containing 2 on its second position"
    case _ => "unknown list"
  }

  // If the pattern does not match anything, it will throw a MatchError
  // Pattern matching will tyr all cases in sequence
}
