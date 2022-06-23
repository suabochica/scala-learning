val data = List(1, 1, 2, 3, 5, 8)

def loopA(values: List[Int]) =
    for x <- values yield x * x

def loopB(values: List[Int]) = 
    (0 to (values.size)).map { x => x * x }

def loopC(values: List[Int]) = 
    for x <- values do x * x

def loopD(values: List[Int]) =
    values.map(x => x * x)

loopA(data)
loopB(data)
loopC(data)
loopD(data)