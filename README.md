# Scala

Scala is an acronym for Scalable Language. It is a moder a multi-paradigm programming language designed to express common programming patterns in a concise, elegant, and type-safe way. Scala is written by [Martin Odersky](https://twitter.com/odersky?lang=en)

Primary goal: you know how to get things done in Scala
- Model business domain
- Implement business logic
- Break down complex problems into smaller, simpler problems
- Be confident that the code you write does what you expect
- Leverage a productive development environment

Secondary goal: you get familiar with the most common general purpose libraries and patterns of code
- Collections
- Error Handling
- Concurrency
- Testing
- Know which tool to pick for which job

> The common thread: How can Scala help you solve business problems?

## Syllabus

| Number    | Description                                                                        |
|-----------|------------------------------------------------------------------------------------|
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
- Use existing Java tools
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

### Homebrew

Run:

```
brew install coursier/formulas/coursier && cs setup
```

Along with managing JVMs, cs setup also installs useful command-line tools:

| Commands  | Description                              |
|-----------|------------------------------------------|
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

### sdkman

SDKMAN! is a tool for managing parallel versions of multiple **Software Development Kits** on most Unix based systems. It provides a convenient Command Line Interface (CLI) and API for installing, switching, removing and listing Candidates. 

First step, install sdkman. For that, open a new terminal and enter:

```
curl -s "https://get.sdkman.io" | bash
```

Follow the instructions on-screen to complete installation.
Next, open a new terminal or enter:

```
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

Lastly, run the following code snippet to ensure that installation succeeded:

```
sdk version
```
If all went well, the version should be displayed. Something like:

```
  sdkman 5.15.0
```

Second step, install Scala running the next command:

```
sdk install scala
```

Alternatively, you can install a specific scala's version. To validate the available version run:

```
sdk list scala
```

And you will get the next out

```
================================================================================
Available Scala Versions
================================================================================
     3.1.3               2.13.0              2.12.2              2.10.6         
     3.1.2               2.12.16             2.12.1              2.10.5         
     3.1.1               2.12.15             2.12.0              2.10.4         
     3.1.0               2.12.14             2.11.12             2.10.3         
     3.0.2               2.12.13             2.11.11             2.10.2         
     3.0.1               2.12.12             2.11.8              2.10.1         
     3.0.0               2.12.11             2.11.7                             
     2.13.8              2.12.10             2.11.6                             
     2.13.7              2.12.9              2.11.5         
```

To install the version 2.13.8 of scala, run:

```
sdk install scala 2.13.8
```

Lastly, run the following code snippet to ensure that installation succeeded:

```
scala -version
```

If all went well, the version should be displayed. Something like:

```
Scala code runner version 2.13.8 -- Copyright 2002-2021, LAMP/EPFL and Lightbend, Inc.
```

## Install sbt on macOS

> Note: For this section is assumend that sdkman is already installed

Install a specific version of `sbt` is a similar process of what we did installed scala via sdkman.

View the available SBT versions with:

```
sdk list sbt
```

you will get the next output:

```
================================================================================
Available Sbt Versions
================================================================================
     1.4.2               1.3.1               1.1.0
     1.4.1               1.3.0               1.0.4
     1.4.0               1.2.8               1.0.3
     1.4.0-RC2           1.2.7               1.0.2
     1.3.13              1.2.6               1.0.1
     1.3.12              1.2.5               1.0.0
     1.3.11              1.2.4               0.13.18
     1.3.10              1.2.3               0.13.17
     1.3.9               1.2.1
     1.3.8               1.2.0
     1.3.7               1.1.6
     1.3.6               1.1.5
     1.3.5               1.1.4
     1.3.4               1.1.2
     1.3.3               1.1.1
```

Install SBT 1.5.0-RC2 with: 

```
sdk install sbt 1.5.0-RC2
```

After installing, respond with a “Y” to set 1.5.0-RC2 as your default SBT.

## Run exercises

Open a new terminal and navigate to the project exercise:

```
cd scala-learning/exercises/democracy 
```

Open the project with Visual Code and the Metals plugin installed on it from terminal:

```
code .
```

The first time you open the project with Visual Code will ask "Import Build?" in a dialog. Accept the suggestion and wait that metals build the project.

