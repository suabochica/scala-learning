# Reasoning About Code, Testing
The recommendation in Scala is to use immutable data types by default. Indeed, they have desirable properties such as being thread-safe and allowing local reasoning. However, in some cases, mutable variables lead to simpler code, as you will see in the lectures. We then dive into different testing techniques such as unit testing, property-based testing, integration testing, and mocking.

#### Learning Objectives
- Understand the tradeoffs of using mutable values vs immutable values
- Write unit tests
- Write property-based tests
- Write mocks
- Write integration tests

## Reasoning About Code

### Reasoning About Code

Let's see an example of a program that is badly designed and requires you to know about its implementation details to avoid making mistakes when you called the program. Let's draw a smiley.

We can break down the problem into simpler parts:
- draw the eyes;
- draw the mouth;

Here is how we can draw an eye on an HTML Canvas:

```scala
def drawEye(x: Double, y: Double): Unit =
    graphics.beginPath()
    graphics.arc(x, y, 15, 0, math.Pi * 2)
    graphics.stroke()
    graphics.fillStyle = "blue"
    graphics.fill()
```

And here is how we can draw a mouth:

```scala
def drawMouth(x: Double, y: Double): Unit =
    graphics.beginPath()
    graphics.arc(x, y, 200, math.Pi / 4,3 *  math.Pi / 4)
    graphics.lineCap = "round"
    graphics.lineWidth
    graphics.strokeStyle = "red"
    graphics.stroke()
```

Then we can combine them to create a draw smiley:

```
drawEye(170, 150)
drawEye(330, 150)
drawMouth(250, 200)
```

But now consider the following two variations of the final program.

```
drawEye(170, 150)       drawMouth(250, 200) 
drawEye(330, 150)       drawEye(170, 150)
drawMouth(250, 200)     drawEye(330, 150)
```

Can we switch the order of those lines without changing the meaning of the program? **We cannot**.

So what's happening there? Why does swapping those lines change the meaning of the program? Are these lines not independent of each other? Actually no, the two methods `drawMouth` and `drawEye`, share the same global graphic context. 

The fact that both methods `drawMouth` and `drawEye` depends on the same mutable object `graphics`, prevents us from reasoning locally about these methods.

Indeed, we can't just expect `drawEye` to do what we read in its implementation, we also have to make sure that the `graphics` context is in the right state before we call `drawEye`.

The strategy I've been using to manage complexity was to break down complex programs into smaller programs and to combine them. 

This strategy aims at reducing the cognitive load to reason about programs because you only need to reason on a subset of the whole system. 

However, if local reasoning is not possible, for instance (ie, if combining programs requires knowledge about their implementation internals), then this strategy breaks down. So how can we prevent these problems from happening?

### Refactoring-Proof Programs

Let's elaborate on the root cause that made it harder to reason about code in the previous section. You wiil see how to design programs support local reasoning.

In the previous example, we saw that it is sometimes difficult to reason about the meaning of programs. Now, we will go one step further and identify two types of programs and the assumptions we can make (or not) to reason about them.


Consider the following program that computes the area of a square window of a size randomly chosen between the values 1, 2 and 3 meters:

 ```scala
 def windowSide = util.Random.between(1, 4) // util.Random provided by scala
 def windowArea = windowSide * windowSide

 println(windowArea)
 ```

What does the expression `windowArea` evaluate to? The problem is that the expression `windowSide` by `windowSide` evaluates `windowSide` two times, and each evaluation possibly returns a different random value. 

So we can "fix" the problem by using `val` definition instead of a `def` definition, so that the result of the first evaluation of. But, why does changing `def` into `val` have such an important impact on the behavior of the program?

Actually, the fact that evaluating the expression `Random.between(1, 5)` several times returns possibly different results has important consequences on our ability to reason about it. In particular, we must be very careful when we refactor the code that chooses Random.between.

Check the following algebraic identity:

```
x + x = 2 * x
```

It suggest that we could refactor any expresion of the form "x + x" into " 2 * x" without changin its meaning. For instance:

```
42 + 42 == 2 * 42
```

However;

```
Random.between(1, 4) + Random.between(1, 4) == 2 * Random.between(1, 4)
```

It is not true. Here we enter into the dilema of doing vs describing. In addition to returning a value these operations also "do something" out of the control of the program (in this case, changing the internal state of the `Random` objects.

We say that these operations have **side-effects**. We can classigy them in two categories:

- operations that modify the state of the program
- operations that communicate with the "outside world" (printing to a file, rending from a socket, etc.)

By contrast, operations that only describe what results to return given a set of input parameters are always refactoring-proof. They are also called "purely functional" or "referentially transparent" in the literature.

The example with `windowSide` and `windowArea` was a bit simplistic. But in large code bases, operations with side-effects can be hard to manage because small changes can have large and unexpected consequences.

 By contrast, referentially transparent operations are not subject to these problems because they do nothing more than returning a result that only depends on their input parameters

 To summarize, we have seen that operations that have side-effects open the door to a class of issues that do not exist with referentially transparent operations. As a consequence, side-effecting operations should be used with extra care.

### A Case for Side-Effects

In this section let's discuss the trade-off of alternatives to side-effects. 

Previously, we have seen that side-effecting operations have drawbacks that requires developers to manipulate them with extra care, but *why do we have side-effects at all?*

Let's try to implement a random number generator without using side-effects.

```scala
class Generator(previous: Int):
    def nextInt(): (Int, Generator) =
        val result = previous * 22_695_477 + 1
        (result, Generator(result))
    end Generator

object Generator:
    def init: Generator = Generator(42) // We use a linear congruential generator.
```

The operation `nextInt` returns a random `Int` value and the next `Generator` to use:

```scala
val gen1 = Generator.init
val (x, gen2) = gen1.nextInt()
println(x) // 953210035
val (y, _) = gen1.nextInt()
println(y) // 953210035
val (z, gen3) = gen2.nextInt()
println(yz // -570911984
```

The `between` operation can be implemented in the class `Generator`:

```scala
def between(x: Int, y: Int): (Int, Generator) =
    val min = math.min(x, y)
    val delta = math.abs(x - y)
    val (randomValue, nextGenerator) = nextInt()
    ((randomValue % delta) + min, nextGenerator)
```

Then, the random number generator can bbe used like this:

```scala
val gen1 = Generator.init
val (windowSide, _) = gen1.between(1,4) // windowSide = 2
val windowArea = windowSide * windowSide // : Int = 4
```

However, one drawback of this approach is that we must be careful to use each generator **only once**. The problem becomes worse if several parts of our program use random number generators.

```scala
val gen1 = Generator.init
getSomething(gen1, getSomeOtherThing(gen1))
```

Here, by passing `gen1` at two places we will get same random numbers in both places.

The correct way to use our random number generator is the following:

```scala
val gen1 = Generator.init
val (otherThin, gen2) = getSomeOtherThing(gen1)

getSomething(gen2, otherThing)
```

The method `getSomeOtherThing` needs to also return the next generator to use. When we call `getSomething`, we must be careful to use `gen2` and not `gen1`, of course.

Arguably, this is more tedious to write than using the side-effecting number generator:

```scala
getSomething(getSomeOtherThing())
```

To summarize, "pure" alternatives to side-effect introduce accidental complexity by requiring developers to explicitly carry over the "context" they operate on.

Some techniques exist to simplify this task, but they always come with some constraints.

There is no silver bullet, just choose the approach that works best for you by taking into account their pros and cons.

### Mutable Objects

## Testing

### Unit Testing

### Property-Bases Testing

### Mocking

### Integration Testing

### Testing the Tests

### Debugging

## Assestment

Quickcheck, please check the contents is in `exercises/quickcheck`.