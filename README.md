# Scala

Scala is an acronym for Scalable Language. It is a moder a multi-paradigm programming language designed to express common programming patterns in a concise, elegant, and type-safe way. Scala is written by [Martin Odersky](https://twitter.com/odersky?lang=en)

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
## Data Types

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