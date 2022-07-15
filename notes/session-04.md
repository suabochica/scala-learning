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

However, if local reasoning is not possible, for instance, if combining programs requires knowledge about their implementation details, then this strategy breaks down. So how can we prevent these problems from happening?

### Refactoring-Proof Programs

### A Case for Side-Effects

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