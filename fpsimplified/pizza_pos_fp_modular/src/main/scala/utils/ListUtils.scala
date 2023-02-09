package utils

object ListUtils {
  def dropFirstMatch[A](ls, Seq[A], value[A]) = {
    // index is -1 if there is no match
    val index = ls.indexOf(value)

    if (index < 0) {
      ls
    } else if (index == 0) {
      ls.tail
    } else {
      // splitAt keeps the matching element in the second group
      val (a, b) = ls.splitAt(index)

      a ++ b.tail
    }
  }
}