package rockthejvm

object FunctionalProgramming extends App {
  // Scala is 00
  class Person(name: String) {
    def apply(age: Int) = print(s"I have aged $age years")
  }

  val jim = new Person("Jim")
  jim.apply(38)
  jim(38) // Invoking jim as a function == jim.apply(38)

  /*
   Scala runs on the JVM
   Functional Programming:

   - Compose functions
   - Pass functions as args
   - return functions as results

   Conclusion: FunctionX = Function1, Function2, ..., Function22
   (Scala functions only receives a max of 22 arguments)
   */

  val simpleIncrementer = new Function[Int, Int] {
    override def apply(arg: Int): Int = arg + 1
  }

  simpleIncrementer.apply(23) // 24
  simpleIncrementer(23) // simpleIncrementer.apply(23), a defined function

  // Conclusion: All Scala functions aer instances of these FunctionX types

  // Example of a function with two arguments and a String return type
  val stringConcatenator = new Function2[String, String, String] {
    override def apply(arg1: String, arg2: String): String = arg1 + arg2
  }

  stringConcatenator("I love", "Scala") // "I love Scala"

  // Syntax Sugar
  // ------------
  // A term for more concise syntax that provides
  // the same functionality for something that already exists.
  // No new functionality is introduced.

  val doubler: Int => Int = (x: Int) => 2 * x
  doubler(4) // 8

  // It is equivalent to doublerLonger.

  val doublerLonger: Function[Int, Int] = new Function1[Int, Int] {
    override def apply(x: Int): Int = x * 2
  }
  doublerLonger(4) // 8

  // Higher-Order Function (HOF)
  // ---------------------------
  // functions that takes functions as arguments or
  // return function as results
  val aMappedList: List[Int] = List(1, 2, 3).map(x => x + 1) // prints List(2, 3, 4)
  val aFlatMappedList = List(1, 2, 3).flatMap(x => List(x, 2 * x)) // prints List(1, 1, 2, 4, 3, 9)
  val aFilteredList = List(1, 2, 3, 4 ,5).filter (_ <= 3) // prints List(1, 2, 3)
  //                                                ☝ === (x => x <= 3)

  // Exercise:
  // Given the List(1, 2, 3) and List("a", "b", "c")
  // return a list with all the pairs between both list
  // output: List("1-a", "1-b", "1-c", "2-a", "2-b", "2-c", "3-a", "3-b", "3-c")
  val allPairs = List(1, 2, 3)
    .flatMap(number => List('a', 'b', 'c')
      .map(letter => s"$number-$letter")
    )

  // for comprehensions version
  val allForPairs = for {
    number <- List(1, 2, 3)
    letter <- List('a', 'b', 'c')
  } yield s"$number-$letter"

  // Collections
  // -----------

  // Lists
  val aList = List(1, 2, 3, 4, 5)
  val firstElement = aList.head
  val restOfTheElements = aList.tail
  val aPrependedList = 0 :: aList // List(0, 1, 2, 3, 4, 5,)
  val aExtendedList = 0 +: aList :+ 6 // List(0, 1, 2, 3, 4, 5, 6)

  // Sequences
  val aSequence: Seq[Int] = Seq(1, 2, 3) // Set.apply(1, 2, 3)
  val accessedElement = aSequence(1) // The element at index 1: 2

  // Vectors, a fast implementation of Seq
  val aVector = Vector(1, 2, 3, 4, 5)

  // Sets, No duplicates
  val aSet = Set(1, 2, 3, 4, 1, 2, 3) // Set(1, 2, 3, 4)
  val setHas5 = aSet.contains(5) // false
  val anAddedSet = aSet + 5 // Set(1, 2, 3, 4, 5)
  val aRemovedSet = aSet - 3 // Set(1, 2, 4)

  // Ranges
  val aRange = 1 to 100
  val twoByTwo = aRange.map(x => 2 * x).toList // List(2, 4, 6, ..., 200)

  // Maps
  val aPhoneBook: Map[String, Int] = Map(
    ("Jim", 3456543),
    ("Pam", 9870789)
  )

  // ☝ both definitions are equivalent

  val aPhoneBookArrowSyntax: Map[String, Int] = Map(
    "Jim" -> 3456543,
    "Pam" -> 9870789
  )
}

