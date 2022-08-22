package quickcheck

import org.scalacheck.*
import Arbitrary.*
import Gen.*
import Prop.*

trait HeapProperties(val heapInterface: HeapInterface):

  // Import all the operations of the `HeapInterface` (e.g., `empty`
  // `insert`, etc.)
  import heapInterface.*

  val minOfTwo: (String, Prop) =
    "the minimum of a heap of two elements should be the smallest of the two elements" ->
    forAll { (x1: Int, x2: Int) =>
      val heap = insert(x2, insert(x1, empty))
      val min = if x1 <= x2 then x1 else x2
      findMin(heap) == min
    }

  val deleteMinOfOne: (String, Prop) =
    "delete minumum of heap of one element should return an empty heap" ->
    forAll { (x: Int) =>
      // create a heap with exactly one element, `x`
      val heap1: List[Node] = insert(x, heapInterface.empty)
      // delete the minimal element from it
      val heap0: List[Node] = deleteMin(heap1)
      // check that heap0 is empty
      isEmpty(heap0)
    }

  val insertMinAndGetMin: (String, Prop) =
    "inserting the minimal element and then finding it should return the same minimal element" ->
    forAll(generatedHeap.suchThat(heap => !isEmpty(heap))) { (heap: List[Node]) =>
      // find the miniminal element of the heap
      // (you don’t need to handle the case of empty heaps because it has been excluded from the heap generator)
      val min: Int = if isEmpty(heap) then 0 else findMin(heap)
      // insert the minimal element to the heap
      val updatedHeap: List[Node] = insert(min, heap)
      // find the minimal element of the updated heap should return the same minimal element
      findMin(updatedHeap) == min
    }

  val deleteAllProducesSortedList: (String, Prop) =
    // recursively traverse the heap
    def check(heap: List[Node]): Boolean =
      // if the heap is empty, or if it has just one element, we have
      // successfully finished our checks
      if isEmpty(heap) || isEmpty(deleteMin(heap)) then
        true
      else
        // find the minimal element
        val x1: Int = findMin(heap)
        // delete the minimal element of `heap`
        val heap2: List[Node] = deleteMin(heap)
        // find the minimal element in `heap2`
        val x2: Int = findMin(heap2)
        // check that the deleted element is less than or equal to the
        // minimal element of the remaining heap, and that the remaining
        // heap verifies the same property (by recursively calling `check`)
        val checked: Boolean = x1 <= x2 && check(heap2)
        checked
    // check arbitrary heaps
    "continually finding and deleting the minimal element of a heap should return a sorted sequence" ->
    forAll { (heap: List[Node]) =>
      check(heap)
    }

  val meldingSmallHeaps: (String, Prop) =
    "melding a heap containing two low values with a heap containing two high values" ->
    forAll { (x: Int, y: Int) =>
      val minVal = x.min(y)
      val maxVal = x.max(y)
      // create two heaps:
      // - the first has two duplicate elements inserted, where both are equal to the
      //   highest value among `x` and `y`
      val minHeap = insert(minVal, insert(minVal, empty))
      // - the second also has two duplicate elements insterted, where both are equal
      //   to the lowest value among `x` and `y`
      val maxHeap = insert(maxVal, insert(maxVal, empty))
      // finally, meld both heaps.
      val meldedHeap: List[Node] = meld(minHeap, maxHeap)
      // check that deleting the minimal element twice in a row from the melded heap,
      // and then finding the minimal element in the resulting heap returns the
      // highest value
      val heap1 = deleteMin(deleteMin(meldedHeap))
      val deleteTwoMinAndFindMin: Boolean = findMin(heap1) == maxVal
      // check that inserting the lowest value to the melded heap, and then
      // finding the minimal element returns the lowest value
      val insertMinAndFindMin: Boolean = findMin(insert(minVal, heap1)) == minVal
      // check that both conditions are fulfilled
      deleteTwoMinAndFindMin && insertMinAndFindMin
    }

  // Given two arbitrary heaps, and the heap that results from melding
  // them together, finding the minimum of the melded heap should return
  // the minimum of the two source heaps. Then, continuously deleting
  // that minimum element (from both the melded heap and the source heap
  // that contained it) should always give back a melded heap whose
  // minimum element is the minimum element of one of the two source
  // heaps, until the two source heaps are empty.
  //
  val meldingHeaps: (String, Prop) =
    // Hint 1: write an auxiliary (recursive) method checking that the melded
    // heap is valid with respect to its two source heaps.
    def check(
      heap1: List[Node],
      heap2: List[Node],
      melded: List[Node]
    ): Boolean =
      if isEmpty(melded) then
        isEmpty(heap1) && isEmpty(heap2)

      // Hint 2: that auxiliary method should handle four cases:
      else
        (isEmpty(heap1), isEmpty(heap2)) match
          //  1. the melded heap is empty (which should happen only if the two source
          //     heaps were empty),
          case (true, true) => isEmpty(melded)
          //  2. the minimum of the melded heap is the minimum of the first source
          //     heap (then, check that after removing the minimum from the melded
          //     heap and from the first source heap, the resulting heaps are still
          //     valid),
          case (false, true) =>
            findMin(heap1) == findMin(melded) && check(
              deleteMin(heap1),
              heap2,
              deleteMin(melded)
            )
          //  3. the minimum of the melded heap is the minimum of the second source
          //     heap (then, check that after removing the minimum from the melded
          //     heap and from the second source heap, the resulting heaps are still
          //     valid),
          case (true, false) =>
            findMin(heap2) == findMin(melded) && check(
              heap1,
              deleteMin(heap2),
              deleteMin(melded)
            )
          //  4. all the other cases (which should not happen in correct heap
          //     implementations).
          case (false, false) =>
            if findMin(melded) == findMin(heap1) then
              check(deleteMin(heap1), heap2, deleteMin(melded))
            else
              check(heap1, deleteMin(heap2), deleteMin(melded))
    "finding the minimum of melding any two heaps should return the minimum of one or the other of the source heaps" ->
    forAll { (heap1: List[Node], heap2: List[Node]) =>
      check(heap1, heap2, meld(heap1, heap2))
    }

  // Random heap generator (used by Scalacheck)
  given generatedHeap: Gen[List[Node]]
  given Arbitrary[List[Node]] = Arbitrary(generatedHeap)

end HeapProperties
