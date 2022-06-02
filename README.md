# Scala

Scala is an acronym for Scalable Language. It is a moder a multi-paradigm programming language designed to express common programming patterns in a concise, elegant, and type-safe way. Scala is written by [Martin Odersky](https://twitter.com/odersky?lang=en)

Primary goal: you know how to get things done in Scala
- Model business domain
- Implement business logic
- Break down complex problems into smaller, simpler problems
- Be confident that the code you write does what you expect
- Leverage a productive development environment

Secondary goal: you get familiar with the most commont general purpose librarin and patterns of code
- Collections
- Error Handling
- Concurrency
- Testing
- Know which tool to picjk for which job

> The common thread: How can Scala help you solve bussines problems?

## Syllabus

| Number    | Description                                                                        |
| --------- | ---------------------------------------------------------------------------------- |
| Session 1 | Language syntax, definitions, expressions, conditions, evaluation, domain modeling |
| Session 2 | Generic data types, loops                                                          |
| Session 3 | Build tools, developer workflow, modules, encapsulation                            |
| Session 4 | Reasoning about code, testing                                                      |
| Session 5 | Let the compiler write code for you based on type information                      |
| Session 6 | Error handling, concurrency                                                        |

## Feature
- Statically Typed
- Runs on JVM, full inter-op with Java
- Object Oriented
- Functional
- Dynamic Feature
- Scala blends object-oriented and functional programming in a statically typed languages (something that also offer JavaScript/TypeScript)

## Why learn Scala?

Because is practical and offer the next pros:

- Can be used as drop-in replacement for Java
- Mixed Scala/Java projects
- Use existing Java libs
- Use exisiting Java tools
- Decent IDE Support

## What is Scala Build Tool (SBT)?

SBT is an open source build tool written in Scala for Scala and Java projects, similar to Java's maven. Below are some feature provided by SBT:

- Native support for compiling Scala code.
- Uses Apache Ivy for dependency management.
- Only-update-on-request model.
- Full Scala language support for creating tasks.
- Support mixed Java/Scala projects.
- Launch Read-Eval-Print Loop (REPL) in project context.

## Install Scala on macOS

Run:

```
brew install coursier/formulas/coursier && cs setup
```

Along with managing JVMs, cs setup also installs useful command-line tools:

| Commands  | Description                              |
| --------- | ---------------------------------------- |
| scalac    | the Scala compiler                       |
| scala     | the Scala REPL and script runner         |
| scala-cli | Scala CLI, interactive toolkit for Scala |
| sbt, sbtn | The sbt build tool                       |
| amm       | Ammonite is an enhanced REPL             |
| scalafmt  | Scalafmt is the Scala code formatter     |

For more information about cs, read coursier-cli documentation.

Test your setup running:

```
scala --version
```

should get:

```
Scala code runner version 3.1.2 -- Copyright 2002-2022, LAMP/EPFL
```

## Elements of Programs

A program **expresses** a **computation**. For instance: What is the result of adding one to one?

```
1 + 1
```

**Evaluating** the program "1 + 1" returns the values "2".

In this program:
- `1` is a **literal**.
- `1 + 1` is an **expression** combining two literals wiht the **operation** +


Follow this line, lets check another example:

```
"Hello, world".length
```

- `"Hellow, world"` is a literal
- The operation `length` is apply to it

**Text literals** are distinguised from **names** by the enclosing double quotes.

Now, lets review some examples of operations:

| Program             | Result  |
| ------------------- | ------- |
| 1 > 0               | true    |
| 1 == 0              | false   |
| 1.max(0)            | 1       |
| -5.abs              | 5       |
| "#" * 3             | "###"   |
| "Alice".toUpperCase | "ALICE" |
| true && true        | true    |
| true.&&(true)       | true    |

Operations can be applied to values by using **dot-notation** or by using **infix syntax**. when infix syntax is in use, arithmetic operators have the same **precedence** as in mathematics.

```scala
1 + 2 * 3
1.+(2.*(3))
```
The result of arithmetic operations has the type of its widest operand:

```scala
1 + 2 // Int
1 + 2.0 // Double
```

Large expressions are hard to read an write.

We can **give names** to fragments of expressions and then refer to them by using these names:

```scala
val facade = 5 * 3
val door = 1 * 2

facade - door
```

Names are introduced with the `val` keyword. Naming an expression makes it easy to **reuse** it multiple times.

In summary:

- Programs *express* computations.
- Programs are made of *values* combined together with *operations*.
- Intermediate results can be *named* to be easily reused.

## Types
Types are the rules to get expressions together. So, expressions have types. In the next example we will highlight an error:

```scala
true && "false"
        ˆˆˆˆˆˆˆ
error: type mismatch;
    found: String ("false")
    required: Boolean
```

Types classify values. Another way to look at it is to say that type defines a **set of possible values**. For instance, the type `Boolean` has two possible values: `true` and `false`. We will discover some other types later. For now the next table is a initial reference.


| Type    | Description                                 |
| ------- | ------------------------------------------- |
| Boolean | true or false                               |
| Byte    | 16 bit signed value                         |
| Short   | 16 bit signed value                         |
| Char    | 16 bit unsigned Unicode character           |
| Int     | 32 bit signed value                         |
| Long    | 64 bit signed value                         |
| Float   | 32 bit IEEE 754 single-precision float      |
| Double  | 64 bit IEEE 754 single-precision float      |
| String  | A sequence of characters                    |
| Unit    | Corresponds to no value                     |
| Null    | Empty reference                             |
| Nothing | Sub-type of every other type; inclueds no   |
| Any     | The supertype of any type; any object is of |
| AnyRef  | The supertype of any reference type;        |

The types define how the expression can be combined by applying operations to them. For this reason, operations are also called **members** of types.

For instance, the `&&` operation is available on the type Boolean and expects another Boolean value on its right-hand side. We can say that the type Boolean has a member named `&&`, which takes another Boolean value as parameter.

If you try to apply an operation to an expression whose types does not provide such an operation, it is an error:

```scala
true.combine(false)
    ˆˆˆˆˆ
error: value combine is not a member of Boolean
```

The Scala compiler can check the programs are **well typed** before they are evaluated. This ensures that some kind of errors cannot happen at run-time.

Note that in worksheet, there is no distinction between compilation and evaluation, but you will see the difference between compilation errors and run-time errors when you will work on Scala projects outside of worksheets.

Type are generallly inferred, but we can explicitly indicate the type of a definition:

```scala
val facade: Int = 5 * 3
```

This can sometimes improve readability.

In summary:

- Types define the rules for combining expressions togehter.
- Before a Scala program is executed, the compiler check that it is well typed.
- The compiler is usually able to infer the types of the definitions of a program, but you can add them explicitly to improve code readability.

## Methods and Parameters

Lets check how we can applay the same program to different input values by defining methods that get parameters.

Consider that you will build two houses one form Michael and other for Holly. Each house have one door and two windows, however they do not have the same size. And intial approach could be:

```scala
// Michael's House
val facade = 5 * 3
val door = 2 * 1
val window = 1 * 1

// Holly's House
val facade = 4 * 4
val door = 2 * 1
val window = 1.5 * 1
```

Note how **similar** these twos programs are.

> Where **similar** functions are carried out by distinct piece of code, it is generally beneficial to combine them into one by abstracting out the **varying** parts. Benjamin C. Pierce

The benefits are:
- Reduce maintenance: the implementation is centralized in one place.
- Structures code into abstractions levels.

So for this case, what are the varying parts? Answer, the areas of the windows and facades. We can **abstract out** the facade and windows areas by defining a **method**:

```scala
val door = 2 * 1

def house(facade: Double, window: Double) =
    facade - door - window * 2
```

And then, we can compute the area of each houses in the next way:

```scala
// Michael's House
house(5 * 3, 1 * 1)

// Holly's House
house(4 * 4, 1.5 * 1)
```
Below we highlight some keypoints:
- Methods are introduced by the `def` keyword
- The method `house` takes two parameters: `facade` and `window`
- We have to specify the type of method parameters

The body of a method can span several lines:

```scala
def house(facade: Double, window: Double): Double =
    val door = 2 * 1
    val subtractedArea = door + window * 2
    facade - subtractedArea
end house
```

This is useful to introduce intermediate definitions before returning the result.

All the lines with the same *level of identation* make a block. The block end with the resulting expression. The `end` marker is optional but their purpose is only to improve readability

In Scala 2 blocks had to be delimited by braces `{}` and Scala 3 still supports this syntax. Also we can explicit result type.

Another concept to keep in mind is the **lexical scope**. It is worth nothin that name introduced within a block are not visible from the outside of the block. So in the next example:

```scala
def tenSquared: Int =
    val ten = 10
    ten * ten

ten // error: not found: value ten
```

In summary, parameters allow us to:
- Implement a program with unknown inputs
- **apply** the same program to different sets of inputs

**Blocks** let us write intermediate definitions befor returnin a result. Definitions that are inside the block are not visible outside of the block.

## Variables

For mutable values:

```scala
var a : Int = 32
```

For immutable values:

```scala
val a : Int = 21
```

For variable expressions:

```scala
val x = { val a : Int = 200; val b : Int = 300; a + b}
x: Int = 500
```

```scala
val a : Int = 21
```

for lazy initialization; initialize the value when is demanded:

```scala
lazy val y = 500
y: int = <lazy>
```

## String Interpolation

The string interpolation is when a define variable in a given string return its value.

```scala
object HelloWorld {
    def main(args: Array[String]) {
        val name = "michael"
        val age = 18;

        println(name + "is" + age + "years old");
        println(s"$name is $age years old"); // 👈🏾 String interpolation
        // prints: michael is 18 years old

        println(f"$name%s is $age%f years old"); // 👈🏾 String interpolation defining type
        // prints: michael is 18.0000 years old

        println(s"Hello \nWorld"); // 👈🏾 String break line
        // prints: Hello
        // World

        println(raw"Hello \nWorld"); // 👈🏾 String raw
        // prints: Hello \nWorld
    }
}
```

## Conditions

Conditions are a way to manage alternative branches of computations. Let us say that we want to show the price of the paint to buy. As long as that price is lower than 100 euros, we shouw it, otherwise we show a message "This is too expensive"

```scala
def showPrice(paitingArea: Double, paintPrice: Double): String =
    val price = paintingArea * paintPrice

    if price > 100 then
       "This is too expensive"
    else
        price.toString
```

An `if` expressions takes a condition followed by the keyword `then`, and has two continuation branches separated by `else`. It it worth nothing that the if/else construct is an expression: it evaluates to a value

The conditions must be an expression of type `Boolean`, otherwise it is an error.

In escala two the syntax was a bit different. The conditions was written between parenthesis, and there was no then keyword:

```scala
// Scalar 2 Syntax
def showPrice(paintingArea: Double, paintPrice: Double): String {
    var price = paintingArea * paintPrice;

    if (price > 100) {
        "This is too expensive";
    } else {
        price.toString
    }
}
```

In summary, alternative branches of computations can be implemented by using if expressions.

## Evalauting Definitions

Lets recall a subtle difference between `def` and `val` definitions. It is possilbe to define methods that take no parameters, then what is the difference between those two definitions?

```scala
val tenSquared = 10 * 10
def tenSquared = 10 * 10
```

The difference is that the body of the `def` definitions is evaluated **each time** we use their name. This means that if we never use it, it is neve evaluated.

By contrast, `val` definitions are evaluated **once**, and their result is reused each time their name is used.

So, `def` is accurate to cases for parameterless definitions. A reason to use `def` instead of `val` is to **delay** the evaluation of a computation to a point in the program where we effectively **need** it.

In summary:

- Definitions using `def` are re-evaluated each time they are used. We use them mainly for operations that take parameters.
- Definitions using `val` are evaluated only once. We use them mainly for itermediate expressions.

## Domain modeling

To understand the concept of domain modeling, lets consider the next version of the program that computes the area od a house that needs to be painted:

```scala
val facade = 5 * 3
val door = 2 * 1
val window = 1 * 1

facade - door - window - window
```

Each part of the house, is defiined as a surface which is itself defined in terms of two numbers, a width and a height.

I argue that we –as humans– have to make some cognitive effort to map these numbers to the abstract concept they relate –a shape.

We can say that number are a **low-level** concept, whereas shapes are a **high-level** concept.

Alternatively, here is how we could express our program in terms of _shapes_ and _areas_:

```scala
val facade = Rectangle(width = 5, height = 3)
val door = Rectangle(width = 1, height = 2)
val window = Rectangle(width = 1, height = 1)

facade.area - door.area - window.area - window.area
```

This version is more readable and less error prone. We will see how to define the `Rectangle` concept.

So, this modeling is a fight to find the right level of abstraction. One could object that doors or windows are more complex than rectangles (e.g., a door is made of some material, it has a handle, etc.) However, **for the purpose of our program** a rectansle seems to carry enough information to a model a door.

> The purpose of abstraction is not to be vague, but to create a new semantic level in which one can be absolutely precise, Edsger W. Dijkstra.

To summarize, the act of defining, in a program, the concept of domain that map closely our human reasoning is called **modeling**.

## Case Classes

Lets evaluates a concept that aggregates several concepts together. The concep of a _rectangle_ , which is defined by a _width_ and a _height_, can be modeled as follows in Scala:

```Scala
case class Rectangle(width: Int, height: Int)
```

This definitions introduce a **tyoe** and a **constructor**, both named `Rectangle`.

The type `Rectangle` has two members: `width` and `height`. Both members have type `Int`.

The constructor `Rectangle` takes tow parameters: `width` and `height`. We can construct a value of type `Rectangle` as follows:

```Scala
val facade = Rectangle(5, 3)
```

and the area of the value can be computed as next:

```Scala
facade.width * facade.height
```
Because computing the area of a rectangle is a common operation in our program, we can define it as an operation of the case class `Rectangle`:

```Scala
case class Rectangle(width: Int, height: Int)
    val area = width * height
```

Note that the area is not part of the information that defines a rectangle. Instead, it is _computed_ from the width and the height of the rectangle. That is why it is defined as an operation rather than a parameter of the case class.

The operations of a class must be defined with a higher lever of indentation than the class itself. Below the complete example of the Rectangle case class:

```Scala
case class Rectangle(width: Int, height: Int)
    val area = width * height

val facade = Rectangle(5, 3)
facade.area
```

Keep in mind that case classes area immutable. This means that once you construct a `Rectangle` you cannot modify it:

```Scala
val rectangle = Rectangle(width = 3, height = 4)

rectangle.width = rectangle.width * 2 // Error: Reassignment to val width
```

In Scala, it is idiomatic to work with **immutable data types**. Case classes come with a handy method to create a copu of an existing values with some fields updated:

```Scala
val smallRectangle = Rectangle(width = 3, height = 4)
val largeRectangle = smallRectangle.copy(width = smallRectangle.width * 2)
```

We will discuss later how to create mutable data types, and what their tradeoffs are.

In summary, a _case class_ aggregates several concepts togehter in a new type, and them define immutable data types.

## Sealed Traits

Let's learn about the second tool for modeling bussines domains, _sealed traits_.

Case classes **aggregate** several values into a single concept. Conversely, we can define type that **accept** only one of several alternative values.

For instance we can define a type `Shape` that is either a `Rectangle` or a `Circle` as follows:

```scala
sealed trait Shape
case class Rectangle(width: Int, heihg: Int) extends Shape
case class Circle(radius: int) extends Shape
```

Unlike case class definition, sealed trait definitions do not introduce constructors. We say that traits area **abstract** types.

The only way to construct a value of type `Shape` is to use the constructor of a concrete class that extends `Shape`:

```scala
val someShape: Shape = Circle(5)
```

It is worth nothing that a value of type `Rectangle` or `Circle`, is also of type `Shape`.

```scala
val someRectangle: Rectangle = Rectangle(1, 2)
val someShape: Shape = someRectangle
```

We say that types `Rectangle` and `Circle` are **subtypes** of `Shape`. Subtyping is a topic over we will learn more about later.

The type `Shape` has no members. the only (useful) thing we can do with a value of type `Shape` is to recover its concrete type using **match** expression.

```scala
val someShapeArea =
    someShape match
        case Rectangle(width, height) => width * height
        case Circle(radius) => radius * radius * 3.14
```

In this example, we define the result of `someShaperArea` by listing all the possible cases of `Shape`.

In case `someShape` is a `Rectangle`, then the result of the `match` expression is `width * height`, where these values are extracted from the underlying rectangle

For other hand, the compiler warns you and provide examples of input that whould fail to be matched by your patterns:

```
[warn] Match may not be exhaustive
[warn] It would fail on the following input: _
```

So far we have shown how to recover a concrete type of a value and to extract its fields. There are ohter things we can do when we match on expressions. Here are two alternative patterns that you might find useful.

```scala
    someShape match
        case circle: Circle => s"This is a circle with radius ${circle.radius}"
        case _ => "This is not circle"
```

- The **typed pattern** matches only instances of type `Circle` and binds the name `circle` to the matched values.
- The **wildcard patter** matches everything.

To summarize, a *case class* aggregates several concepts together, whereas a *sealed trait* represents one of seveleral alternatives.

These two building blocks can be used to _model_ bussines domains.

_Match_ expressions can be used to define alternative _branches_ of a program according to the concrete class of a sealed trait, and to _extract_ data from this class at the same time.

## Enumerations

Sometime the alternatice values of a type are not _classes_ of values, but singleton values. Scala 3 provides a special construct for this case:

```scala
enum PrimaryColor:
    case Red, Blue, Green
```

This defines a Prymary color type with exactly three possible values: `PrimaryColor.Red`, `PrimaryColor.Blue` or `PrimaryColor.Green`.

So, we can use **literal patterns** on enumerations:

```scala
def isProblematicForColorBlindPeople(PrimaryColor): Boolean =
    color match
    case PrimaryColor.Red => true
    case PrimaryColor.Blue => false
    case PrimaryColor.Green => true
```

Enumerations provide a `values` operations that enumerate all their possible values:

```scala
PrimaryColor.values // Array(PrimaryColor.Red, PrimaryColor.Blue, PrimaryColor.Green)
```

You can find an enumeration value from its String label:

```scala
PrimaryColor.valueOf("Green") // PrimaryColor = PrimaryColor.Green
```

If the argument passed is not a valid enumeration label, it is a runtime error.

Something to consider is that an enum definition is just syntactic sugar for a sealed trait an case objects that extend this trait, Is this way how we get Scala 2 compatibility:

```scala
enum PrimaryColor:
    case Red, Blue, Green
```
Is equivalent to:

```scala
sealed trait PrimaryColor
objecr PrimaryColor:
    case object Red extends PrimaryColor
    case object Blue extends PrimaryColor
    case object Green extends PrimaryColor
    val values = Array(Red, Blue, Green)
    def valueOf(label: String): PrimaryColor = ...
```

this code defines:
- a type `PrimaryColor`, with a fixed number of possible values
- an **object** `PrimaryColor`, containing the definitions of the values of the type `PrimaryColor`
- the three possible values, `Red`, `Blue`, and `Green` for `PrimaryColor`

Note that the same name `PrimaryColor` can refer to either a type or a value, depending on where it is used.

It refers to the object `PrimaryColor` when it is used on the right-hand side of a definition, or when it is passed as an argument to an operation:

```scala
val color = Primary.Green
isProblematicForColorBlindPeople(PrimaryColor.Green)
```

It refers to the type `PrimaryColor` when it is used on the right-hand side of a type annotation:

```scala
val color: PrimaryColor = Primary.Green
```

A **case object** that extends a _sealed trait_ defines one possible value for that trait.

Unlike a case class, which defines a constructor, a case object **is** already a value.

In summary, `enum` is a convenient construct for modeling a type that has one of several possible singleton values.

An enumeration defines a type and its companion object which in turn defines the possible values of the enumeration, and additional operations such as `values` and `valueOf`.

## Business Logic

Previously were introduced language features such as:

- methods
- conditions
- case clases
- sealed traits

These features are the building blocks for modeling the **business domain** of a program and implementing its **business logic**.

There is no systematic methodology: often, a same set of concepts can be modeled in multiple ways. But, here are some advice:

1. Identify the concepts (in general, nouns) that you are interested in
2. Identify the relations between them
  - Does a concept _belong_ to another one? (e.g. a rectangle has a width and a height)
  - Does a concept _generalize_ to another one? (e.g. a shape can either be a rectangle or a circle)
3. Translate each concept into a type definition
    - Concepts belonging to others become **parameters** of case clasess
    - Concepts generalizing to others become **sealed traits**
4. Check that you can construct meaningful values from your model
    - Conversely, check that you can **not** construct nonsensical values form your model
5. Sometime you need several iterations of domain model an logic implementation to find the right solution.

In summary, reify the concepts (nouns) of a business domain into case clases and sealed tratis.

Check that values derived from your model are valid by construction and that they are meaningful for your problem.

Implement the business logic as operations on your model.
