val data = List(1, 1, 2, 3, 5, 8)

def loopA(values: List[Int]): Int =
  values.foldLeft(0)((accum, elt) => accum + elt)

def loopB(values: List[Int]): Int = {
  var total = 0
  var valuesLeft = values
  while(valuesLeft.nonEmpty) {
    total = total + valuesLeft.head
    valuesLeft = valuesLeft.tail
  }
  total
}

def loopC(values: List[Int]): Int = {
  values match {
     case Nil => 1
     case head :: tail => head * loopC(tail)
  }
}

def loopD(values: List[Int]): Int = {
  values.map(x => x + 1).head
}

loopA(data)
loopB(data)
loopC(data)
loopD(data)