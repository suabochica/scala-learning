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

