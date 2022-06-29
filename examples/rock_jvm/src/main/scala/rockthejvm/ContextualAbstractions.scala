package rockthejvm

object ContextualAbstractions {

  // Context parameters/arguments
  // ---------------------------

  val aList = List(2, 1, 3, 4)
  val anOrderedList = aList.sorted // contextual argument: (descendingOrdering)

  // Ordering
  // given keyword is not working
  val descendingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)

  // Analogous to an implicit val
  trait Combinator[A] {
    def combine(x: A, y: A): A
  }

  /*
  def combineAll[A](list: List[A])(using combinator: Combinator[A]): A =
    list.reduce((a, b) => combinator.combine(a, b))

  given intCombinator: Combinator[Int] = new Combinator[Int] {
    override def combine(x: Int, y: Int): Int = x + y
  }

  val theSum = combineAll(aList)
  */

  // Context bounds
  // ---------------------------


  // Extensions Methods
  // ---------------------------

  def main(args: Array[String]): Unit = {
    println(anOrderedList)
    // println(theSum)
  }
}
