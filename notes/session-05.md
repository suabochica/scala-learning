# Type Directed Programming

Programs automate repetitive tasks. Sometimes, writing a program is a repetitive task. We look into a unique feature of Scala that lets the compiler write some (repetitive) parts of your programs for you! Based on the expected type of a parameter, the compiler can provide a value for this parameter if it finds an unambiguous candidate value. This technique is often used to automatically generate the serializer and deserializer of a data type, solely based on its definition, for instance.

#### Learning Objectives
- Provide simple given instances for a specific type
- Call methods that take context parameters
- Provide conditional given instances
- Use extension methods

## Type-Directed Programming

### Motivation

Let's add some motivaiton for a language feature named typed directed programming or contextual abstractions.

We have seen that the compiler is able to _infer_ types from _values_:

```scala
val x = 43 // x: Int = 42
val y = x + 1 // x: Int = 43
```

The Scala compiler is also able to do the opposite, namely to _infer values_ form _types_. This is useful when there is exactly one "obvious" value for a type, the compiler can provide that value to us. Let's check the next example.

Consider a method sort that takes as parameters a list of int and returns another list of int containing the same elements, but sorted.

```scala
def sort(xs: List[Int]): List[Int] =
    ...
    ... if x < y then ...
    ...
```

At some point, this mehtod has to compare two elements `x` and `y` of the given list.

The problem is: how could we generalize `sort` so that it can also be used for list with elements other than `Int`, such as `Double` or `String`?

A first step is to use a **type parameter** `A` for the type of elements:

```scala
def sort[A](xs: List[A]): List[A]
```

Now, we can call this method with an arbitrary type of list, such as `List[Int]`, `List[Double]` or `List[String]`.

If we call `sort` with a `List[Int]` as parameter, the compiler unifies the type paramete `A` with `Int`, so the return type is `List[Int]`. 

Unfortunately, we can't implement the signature because there is no universal way of comparing values of type `A`. One solution is to pass the comparison operation as an additional parameter:


```scala
def sort[A](xs: List[A])(lessThan:(A, A) => Boolean): List[A] =
    ...
    ... if lessThan(x, y) then ...
    ...
```

We can now call `sort` as follows:

```scala
val xs = List(-5, 6, 3, 1, 7)
val strings = List("apple", "pear", "orange", "pineapple")

sort(xs)((x, y) => x < y) // : List[Int] = List(-5, 1, 3, 6, 7)
sort(strings)((s1, s2) => s1.compareTo(s2) < 0) // : List[String] = List("apple", "orange", "pear", "pineapple")
```
Actually, there is already a class in the standard library that represents orderings. 

```scala
scala.math.Ordering[A]
```

It provides ways to compare elements of type `A`. Instead of parameterizing with `lessThan` function, we could equivalently parameterize with Ordering.

```scala
def sort[A](xs: List[A])(ord: Ordering[A]): List[A] =
    ...
    ... if ord.lt(x, y) then ...
    ...
```

However, passing around Ordering arguments is cumbersome.

```scala
sort(xs)(Ordering.Int)
sort(ys)(Ordering.Int)
sort(strings)(Ordering.String)
```

Sorting a `List[Int]` value always use the same `Ordering.Int` argument, sorting a `List[String]` value always use the same `Ordering.String` argument, and so on...

Simple cases like these are repetitive and tedious. Complex cases like sorting by multiple criteria are really painful to maintain. Can the compiler automate this process for us? Can it find the ordering instance and pass it to the method code?

### Context Parameters

To solve our previous problem of passing around Ordering arguments let's learn how to instuct the compiler to pass parameter for us by defining them as context parameters.

So, we would like to just write:

```scala
sort(xs)
```

And let the compiler pass the ordering for us. To achieve this we need to do two things:

1. let the compiler know that we expect it to pass the argument,
2. provide candidate values for such arguments

In this section we will address the first point

We let the compiler know that we expect it to pass the `ord` argument by making it a **context parameter**.

```scala
def sort[A](xs: List[A])(using ord: Ordering[A]): List[A] = ...
```

Then calls to sort can omit the `ord` argument:

```
sort(xs)
sort(ys)
sort(strings)
```

The compiler infers the argument value based on its expected type. Later we will explain the mechanism used by the compiler to find the respective `Ordering` value.


Note that it is still possible to explicitly pass an argument via a `using` argument clause, which can be usefull to provide a non-default value:

```scala
sort(xs)(using Ordering.Int.reverse)
```

But the argument is usually left out. For reference, multiple context parameters can be in a `using` clause, or can be several `using` clauses in a row. Additionally, `using` clauses can also be freely mxed with regural parameters.

For other side, the parameters of a `using` caluse can be anonymous. This is useful if the body of `sort` does not mention `ord` at all, but simply passes it on as a context argument to further methods.

There is a shorter syntax for context parameters:

```scala
def sort[A: Ordering](xs: List[A]): List[A] = ...
```

We say that the type parameter `A` has one **context bound**: `Ordering`. This is equivalent to the following signature:

```scala
def sort[A](xs: List[A])(using ord: Ordering[A]): List[A] = ...
```

In Summary, parameters marked by a `using` clause can be left out at the call site. The compiler infers their value based on their expected type.

### Given Definitions

Let's explain how to define candidate values that can be picked by the compiler when you call a method that takes context parameters.

Here is a complete example:

```scala
trait Ordering[A]:
    def compare(a1: A, a2: A): Int

// companion object
object Ordering:
    given Int: Ordering[Int] with
        def compare(x: Int, y: Int): Int = ...
    given String: Ordering[String] with
        def compare(s: String, t: String): Int = ...

def sort[A](as: List[A])(using Ordering[A]): List[A] = ...

sort(List(1, 3, 2)) // : List[Int] = List(1, 2, 3)
sort(List("banana", "apple")) // : List[String] = List("apple", "banana")
```


In summary, there has to be a **unique** given instance matching the queried type for it to be used by the compiler as a context argument. 

Given instances are searched in the enclosing **lexical scope** (import parameters or inherited members) as well as in **companion object** of types associated with the query type.

### Priorities Between Given Definitions

### Type Classes

### Conditionals Given Definitions

### Type Directed Programming in Scala 2

## Extension Methods and Implicit Conversions

### Type Classes and Extensions Methods

### Implicit Conversions

## Assestment

Codecs, please check the contents in `exercises/codecs`