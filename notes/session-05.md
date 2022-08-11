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

Here is a complete example with the `given` keyword over the `Int` . and `String` types:

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

One can refer to a named or anonymous instance by its type using the `summon` keyword:

```scala
summon[Ordering[Int]]
summon[Ordering[Double]]
```

These expand to:

```scala
Ordering.Int
Ordering.given_Ordering_Double
```

`summon` is a predifined method. It can be defined like:

```scala
def summon[T](using arg: T): T = arg
```

If we define a function takes a context parameter of type `T`, then the compiler will search a given instance that:

- has a type compatile with `T`
- is visible at the point of the methods call, or is defined in a companion object associated with `T`.

If there is a single definition, it will be taken as actual arguments for the context parameter. Otherwise, it is an error. Below we illustrate the visibility of given instances:

```scala
class Foo

trait Givens: given Foo = Foo()
    given[Foo]

    summon[Foo] // ✔, is defined in the enclosing scope

object Givens extends Givens:
    summon[Foo] // ✔, is inherited from the train Givens

summon[Foo] // ✘, no given instances were found that match type Foo

import Givens.{given Foo}
summon[Foo] // ✔, given Foo is imported
```

Since given instance can be anonymous, there are three ways to import them:

1. By name: `import scala.math.Ordering.Int`
2. By type: `import scala.math.Ordering.{given Ordering[Int]`
3. With a given selector: `import scala.math.Ordering.given`

Here the second form of import is preferred because it is most informative.

The scope of a search for a given instance of type `T` includes:

- first, all the given instance that are visible.
- then, the given instances found in any companion object *associated* with `T`.

The definition of _associated_ is quite general. Besides, the companion object of the type T itself, the compiler will also consider:

- companion objects associated with any of T's ingerited types,
- companion objects associated with any type argument in T,
- if T is an inner class, the outer object in which it is embedded.

If more than one given instance is eligible, an **ambiguity** is reported:

In summary, there has to be a **unique** given instance matching the queried type for it to be used by the compiler as a context argument. 

Given instances are searched in the enclosing **lexical scope** (import parameters or inherited members) as well as in **companion object** of types associated with the query type.

### Priorities Between Given Definitions

When we work with several given instances that match the same type they don't generate an ambiguity if one instance is more specific than the others. So it is useful understand what it means to be more specific.

A definition `given: a: A` is more specific than a definition `given b: B` if:

- `a` is in a closer lexical scope than `b`,
- `a` is defined in a class or object which is a subclass of the class defining `b`,
- type `A` is a subtype of type `B`,
- type `A` has more "fixed" parts than `B`.

Let's review this rules wiht examples:

```scala
// Which given instance is summoned here?

given universal[A]: A = ???
given int: Int ??? // ✔

summon[Int] // The last rule is applied, and `Int` is more fixed than `A`
```

Second example,

```scala
// Which given instance is summoned here?

trait A:
    given x: Int = 0

trait B extends A:
    given y: Int = 1 // ✔ 

object C extends B:
    summon[Int] // The second rule is applied.
```

Third example,

```scala
// Which given instance is summoned here?

given x: Int = 0
def foo() =
    given y: Int = 1 // ✔
    summon[Int] // The first rule is applied.
```

Last example,

```scala
// Which given instance is summoned here?
class General()
class Specific() extends General()


given general: General = General()
given specific: Specific = Specific() // ✔

summon[General] // The third rule is applied.
```

In summary. We have seen that several instances matching the same type don't generate an ambiguity, if one instance is **more specific** than the others, and we have seen what it means to be more specific.

### Type Classes

Time to discuss the two mechanisms to achieve polymorphism, type classes versus sub-typing.

Before we saw a particular pattern of code:

```scala
trait Ordering[A]:
    def compare(a1: a, a2: A): Int

object Ordering:
    given Int: Ordering[Int] with
        def compare(x: Int, y: Int) =
            if x < y then -1 else if x > y then 1 else 9

    given String: Ordering[String] with
        def compare(s: String, t: String) = s.compareTo(t)

def sort[A](xs: List[A])(using Oredering[A]): List[A] = ...
```

We say that `Ordering` is a **type class**. Type classes support _retroactive_ extension: the ability to extend a data type with new operations without changing the original definition of the data type.

Type classes provide yet another form of polymorfism. In the `sort` example the behavior of the logic varies according to the type of the elements in the list.

At compilation-time, the compiler resolves the specific `Ordering` implementation that matches the types of the list elements.


**Subtyping** also provides a way to specialize the behavior of a methods ccording to the type of values it is applied to.

```scala
trait Comparable:
    def compareTo(that: Comparable): Int

case class Rational(num: Int, denom: Int) extends Comparable:
    def compareTo(that: Comparables): Int = ???

def sort(List[Comparable]): List[Comparables] = ???
```

However, in Scala, type classes are often preferred over subtyping to achieve polymorphism.

Let's compare both alternatives:

```scala
// Type Class
trait Ordering[A]:
    def compare(x: A, y A): Int

// Subtyping
trait Comparable:
    def compareTo(that: Comparable): Int
```

In the first case, we compare `x` to `y`, whereas in the second case we compare `this` to `that`. A type class only defines operations about another type, but it does not define `this` type itself.

In summary, type classes classify types by the operations they support. In Scala, this mechanism is often preferred over sub-typing to achieve polymorphism. One reason for this is that type classes support retroactive extension.

### Conditionals Given Definitions

Given definitions can also take context parameters. This allows the compiler to summon complex pieces of logic solely based on type information. Let's consider the next introductory example, where we will consider the ordering of `String` values:

```
"abc" < "abd"

1. a | a = same
2. b | b = same
3. c | d = c < d

Then, the first string is before the second string
```

Here, we compare the characters of each string, element-wise. However, we face the next problem: Strings are sequences of characters, how to generalize the comparison process to sequence of any element type `A`?

This can be done only if the list of elements have an ordering. Given definitions can take type parameters and context parameters:

```scala
given orderingList[A](using ord: Ordering[A]): Ordering[List[A]] with ...
```

`orderingList` is a _conditional_ definition: an ordering for list with element of type `A` exist only if there is an ordering for `A`.

This sort of conditional behavios is best implemented with type classes. Subtyping and inheritance cannot express this: a class either inherits a trait, or, does not.

The compiler follows the next rule to summoning conditional given instances. A given instances for the outer type is constructed first and then its context parameters are filled in turn. So for the next implementation:

```scala
given [A: Ordering]: Ordering[List[A]] with
  def compare(xs: List[A], ys: List[A]): Int = ...
end given

def sort[A](xs: List[A])(using ord: Ordering[A]): List[A] =
  def merge(xs: List[A], ys: List[A]): List[A] =
    ...
  end merge
  ...
end sort

val xss = List(List(1, 2, 3), List(1), List(1, 1, 3))
```

when we call the sort method, like:

```scala
sort(xss)
```

The compiler does:

1. `sort[List[Int]](xss)`: unifies the type parameter A to the List[Int], because that is the type of `xss`
2. `sort[List[Int]](xss)(using orderingList[Int])`: It finds the given definition `orderingList` after unifying its type parameter A with [Int], but orderingList itself takes a context parameter of type [Int].
3. `sort[List[Int]](xss)(using orderingList[Int](using Ordering.Int))`: The compiler now looks for a candidate of type ordering of [Int], it finds the given definition ordering that [Int].

For other side, an arbitrary number of conditional definitions can be combined untile the search hits a "terminal" given definition. 

What happens if we define a conditional instance that depends on itself? Like: 

```scala
trait A 
given loop(using a: A) : A = A

summon[A]
```

Here, a loop takes an `a` and provides an `a`. The compiler detects that there is a cycle and it raises an error that says diverging implicit search when trying to match type `A`.

To summarizeL
- given definitios can also take type parameters and context parameters,
- an arbitrary number of given definitions can be chained until a terminal definition is reached,
- this allows the compiler summon complex pieces of logic based on type information.

### Type Directed Programming in Scala 2

The previous lectures showed how to define context parameters and given definitions in Scala 3.

Although it was possible to achieve the same thing in Scala 2, the underlying mechanism was different. This page shows how to define the equivalent of context parameters and given definitions in Scala 2.

#### Context Parameters

In Scala 2, we used the keyword `implicit` to mark a parameter list
as contextual:

```scala
// Scala 3
def sort[A](as: List[A])(using ordering: Ordering[A]): List[A]

// Scala 2
def sort[A](as: List[A])(implicit ordering: Ordering[A]): List[A]
```

Unlike in Scala 3, in Scala 2 only one parameter list (the last one of the definition) could be marked as contextual.

#### Given Definitions

In Scala 2, the equivalent of given definitions was achieved by marking a regular definition with the qualifier `implicit`:

```scala
// Scala 3
given orderingInt: Ordering[Int] with
  def compare(x: Int, y: Int): Int =
    if x < y then -1 else if x > y then 1 else 0

// Scala 2
implicit object orderingInt extends Ordering[Int] {
  def compare(x: Int, y: Int): Int =
    if (x < y) -1 else if (x > y) 1 else 0
}
// ... or
implicit val orderingInt: Ordering[Int] = new Ordering[Int] {
  def compare(x: Int, y: Int): Int =
    if (x < y) -1 else if (x > y) 1 else 0
}
```

Any regular `val`, `def`, or `object` definition could be marked as `implicit`.

Thus, conditional givens were defined by an `implicit def` taking `implicit` parameters:

```scala
// Scala 3
given orderingPair[A, B](
    using ordA: Ordering[A], ordB: Ordering[B]): Ordering[(A, B)] with
  def compare(x: (A, B), y: (A, B)) = ...

// Scala 2
implicit def orderingPair[A, B](
    implicit ordA: Ordering[A], ordB: Ordering[B]): Ordering[(A, B)] = new Ordering[(A, B:wa)] {
  def compare(x: (A, B), y: (A, B)) = ...
}
```

## Extension Methods and Implicit Conversions

### Type Classes and Extensions Methods

Let's see how to liberate extension methods to provide a nicer syntax to work with type classes.

Previously we have seen that the type classes could be used t retroactively add operations to existing data types.

However, when the operations are defined outside of the data types, they cannot be called like methods on these data type instances.

```scala
case class Rational(num: Int, denom: Int)
given Ordering[Rational] = ...

val x: Rational = ...
val y: Rational = ...

x > y 
//^
// value '>' is not a member of 'Rational'
```

On the other hand, extension methods make it possible to add methods to a type, outside the type definition.

```scala
extension (lhs: Rational)
    def < (rhs: Rational) Boolean =
        lhs.num * rhs.denom < rhs.num * lhs.denom

val x: Rational = ...
val y: Rational = ...

x > y  // It works!
```

Now we have a valid question: How we add the `<` operation to any type `A`  for shich there is a given `Ordering[A]` instance? We achieve it defining the extension method in the trait `Ordering[A]`

```scala
trait Ordering[A]:
    def compare(x: A, y: A): Int
    extension (lhs: Rational)
        def < (rhs: Rational) Boolean = compare(lhs, rhs) < 0
```

And then:

```scala
def sort[A: Ordering](xs: List[A]): List[A] =
    ...
    ... if x < y then ...
    ...
```

Here we have some rules. Extensions methods on an expresion of type `T` are applicable if:

1. they are visible (by being defined, inherited, or imported) in a scope enclosing the point of the application, or
2. they are defined in an object associated with the type `T`,or
3. they are defined in a given instance associated with the type `T`

In our case, the compiler rewrites the call x < y to:

```
ordering.<(x)(y)
```

In summary, leverage extension methods to provide a nice syntax to work with type classes. Extension methods are applicable on values of type `T`, if there is a given instance that is visible at the point of application, or that is defined in a companion object associated with `T`.

### Implicit Conversions

Time to  learn about a mechanism named **implicit conversions** to automatically convert expressions of one type to another type.

This mechanism is usually used to provide more ergonomic APIs. Let's review the implicit conversion through the next example: API for defining JSON documents.

```scala
// {"name": "Paul", "age": 42}
Json.obj("name" -> "Paul", "age" -> 42)
```

First, define a model for the JSON values

```scala
sealed trait Json

case class JNumber(value: BigDecimal) extends Json
case class JString(value: String) extends Json
case class JBoolean(value: Boolean) extends Json
case class JArray(elems: List[Json]) extends Json
case class JObject(fields: (String, Json)*) extends Json
```

With this definition we can do the next representation

```scala
// {"name": "Paul", "age": 42}
JObject("name" -> JString("Paul"), "age" -> JNumber(42))
```

Check that in the constructor of the `JObject` we use an `*` to represent a repeated parameter. Let's go deep with this concept:

```scala
def printSquares(xs: Int*) = println(xs.map(x => x * x))
printSquares(1, 2, ,3) // "Seq(1, 4, 9)"
```

Here we have that:

- `xs` is a *repeated* parameter,
- at call site, we can supply several arguments,
- in the methods body, `xs` has a type `Seq[Int]`,
- repeated parameters can only appear at the end of a parameter list.

Back into our first representation we got the next problem; the constructing JSON objects is too verbose. We expect to support a shorter sytnax.

We could update the type signature of the `obj` constructor to something like:

```scala
def obj(fileds: (String, Any)*): Json
```

but here, we are allowing invalid JSON objects to be constructed. Something like

```scala
Json.obj("name" -> (x: Int) => x + 1)
```

will valid.

We want invalid code to be signaled to the programmer with a compilation error.

Definetively, we have to update the `obj` constructor and to invalidate bad formed JSON object we can use type coercion. We can start with:

```scala
object Json
    def obj(fields: (String, JsonField)*): Json =
        JObject(fields.map(_.json)*)

    case class JsonField(json: Json)
end Json
```

Then we define implicit conversions from basic types we want to support:

```scala
case class JsonField(json: Json)

object JsonField:
    given fromString: Conversion[String, JsonField] with
        def apply(s: String) = JsonField(JString(s))
    given fromInt: Conversion[Int, JsonField] with
        def apply(n: Int) = JsonField(JNumber(n))
    ...
    given fromJson: Conversion[Json, JsonField] with
        def apply(j: Json) = JsonField(j)
```

To be able to use implicit conversion, we have to inform the compiler of our intent by writting the import clause `import scala.language.implicitConversions`.

```scala
import scala.language.implicitConversions`
// {"name": "Paul", "age": 42}
Json.obj("name" -> "Paul", "age" -> 42)
```

Then the compiler implicity inserts the following conversions:

```
Json.obj(
    "name" -> Json.JsonField.fromString.apply("Paul"),
    "age" -> Json.JsonField.fromInt.apply(42),
)
```

The general rule is that the compiler looks for implicit conversions on an expression `e` of type `T` if `T` does not conform to the expression's expected type `S`. In such case, the compiler looks in the context for a given instance of type `Conversion[T, S]`

> Note: at most one implicit conversion can be applied to a given expression.

We have seen that implicit conversions can be useful to implement nice-looking APIs, but they also have drawbacks. 

Indeed, implicit conversions are silently applied by the compiler and they change the type of expressions. 

Therefore, they can confuse developers reading the code. This is why you need to explicitly add the import clause before using implicit conversions. 

Before defining an implicit conversion, make sure to weigh the pros and cons, reducing boilerplate is a good purpose but this should be balanced with the possible drawbacks of not seeing pieces of code that are yet part of the program.

In summary, implicit conversions can improve the ergonomics of an API, but they should be used sparingly.

## Assestment

Codecs, please check the contents in `exercises/codecs`