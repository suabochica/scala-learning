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

A progras **expresses** a **computation**. For instance: What is the result of adding one to one?

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

## If Statements

```scala
object Demo {
    def main(args: Array[String]) {
        val x = 20;
        var result = "";

        if (x == 20) {
            result = "x == 20";
        } else {
            result = "x != 20";
        }

        println(res)

        println(if (x != 20) "x == 20" else "x !- 20");
    }
}