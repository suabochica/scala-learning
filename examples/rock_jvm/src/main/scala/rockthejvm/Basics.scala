package rockthejvm

object Basics extends App {
  // defining a value
  val meaningOfLife: Int = 42 // const int meaningOfLife = 42

  // Int, Boolean, Char, Double, Float, String
  val aBoolean = false // type is optional

  // String and string operations
  val aString = "I love Scala"
  val aComposedString = "I" + "" + "love" + "" + "Scala"
  val anInterpolatedString = s"The meaning of life is $meaningOfLife"

  // Expressions = structure that can be reduced to a value
  val anExpression = 2 + 3

  // if-expression
  val ifExpression = if (meaningOfLife > 43) 56 else 99 // in other language : meaningOfLife > 43 ? 56 : 99

  val chainedIfExpression = {
    if (meaningOfLife > 43) 56
    else if (meaningOfLife < 0) -2
    else if (meaningOfLife > 99) 78
    else 0
  }

  // Code Blocks
  // --------------------------

  val aCodeBlock = {
      // definitions
      val aLocalValue = 67
      // value of block is the value of the las expression
      aLocalValue + 3
  }

  // Function
  // --------------------------

  // define a function
  def myFunction(x: Int, y: String): String = {
    y + "" + x
  }

  // recursive functions
  def factorial(n: Int): Int =
    if (n <= 1) 1
    else n * factorial(n - 1)

  // In Scala we do not use loops or iteration, we use recursion!
  // The Unit type = no meaningful value. "void" in other languages

  // Type of side effects
  print("I love Scala!")

  def myUnitReturningFunction(): Unit = {
    println("I don't love returning Unit")
  }
}
