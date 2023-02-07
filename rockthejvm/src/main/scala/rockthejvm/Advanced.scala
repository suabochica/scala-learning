package rockthejvm
import scala.util.Try
import scala.util.Success
import scala.util.Failure

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Advanced extends App {

  // Lazy Evaluation
  // ---------------
  // Means that an expression is not evaluated until his first use
  // - It is useful in infinite collection

  lazy val aLazyValue = 2
  lazy val lazyWithSideEffect = {
    println("I am so very lazy")
    43
  }

  val eagerValue = lazyWithSideEffect + 1 // prints "I am so very lazy"

  // Pseudo Collection
  // -----------------

  def methodWhichCanReturnNull(): String = "Hello, Scala"
  // Option: A collection which contains at most one element: Some(value) or None
  val anOption = (Option(methodWhichCanReturnNull())) // Some("Hello", "Scala")
  val stringProcession = anOption match {
    case Some(string) => s"I have obtained a valid string: $string"
    case None => "I obtained nothing"
  }

  // Try: A collection with either a value if the code went well, or an exception if the code threw one
  def methodWhichCanThrowException(): String = throw new RuntimeException()

  try {
    methodWhichCanThrowException()
  } catch {
    case exception: Exception => "defend against this evil exception"
  }

  // Instead use

  val aTry = Try(methodWhichCanThrowException())

  val anotherStringProcessing = aTry match {
    case Success(validValue) => s"I have obtained a valid string: $validValue"
    case Failure(exception) => s"I have obtained an exception: $exception"
  }

  // Asynchronous Programming
  // ------------------------

  // Future: Collection which contains a value when it is evaluated
  // - Future is composable with map, flatMap and filter
  val aFuture = Future {
    print("Loading...")
    Thread.sleep(1000)
    println("I have computed a value")
    67
  }

  Thread.sleep(2000)

  // Implicits
  // ---------

  // Warning: Use implicits carefully

  // Case 1: Implicit arguments
  def aMethodWithImplicitArgs(implicit arg: Int) = arg + 1
  implicit val myImplicitInt = 46
  println(aMethodWithImplicitArgs) // aMethodWithImplicitArgs(myImplicitInt)

  // Case 2: Implicit conversions
  implicit class MyRichInteger(n: Int) {
    def isEven() = n % 2 == 0
  }

  println(23.isEven()) // new MyRichInteger(23).isEven()

}
