
# Session 06: Error Handling and Concurrent Programming

The recommendation in Scala is to use immutable data types by default. Indeed, they have desirable properties such as being thread-safe and allowing local reasoning. However, in some cases, mutable variables lead to simpler code, as you will see in the lectures. We then dive into different testing techniques such as unit testing, property-based testing, integration testing, and mocking.

#### Learning Objectives
- Understand the tradeoffs of using mutable values vs immutable values
- Write unit tests
- Write property-based tests
- Write mocks
- Write integration tests

## Error Handling

### Error Handling

Let's check a low level mechanism to handle unexpected errors. So far, we have overlooked error handling. When you run a program, what con go wrong?

- error in input, user can fix,
- programming errors, programmer can fix,
- hardware failure, external systems not working, and so on.

In all these cases, the program cannot return a result. What we can do then?

- abort the program,
- Report the errors to te user or the programmer,
- Retry, implement some form of resilience.

About reporting, to the user, we want to give informative error message, so they can fix bad input. To the programmer, we want to give detailed information that relates to the source code, to ais with debugging.

In practice, when something unexpeced happens, the execution flow of the program can be interrupted. The low-level language mechanis that is involved is called **exceptions**.

Exceptions can be thrown at any point of the execution of the program. They interrupt the execution flow unless they are **caught** by a surrounding exception handler.

In Scala, you can throw an exception by using a **throw expression**:

```scala
def attemptSomething(): Unit =
    println("So far, so good")
    println("Still there")
    throw Runtime Exception("We cannot continue")
    println("You will never see this")
```

> Note: unlike Java, there is no notiong of "checked" exception in Scala. Any methods can throw any exceptions at any time without having to declare it it its signature.

There is a default exception handler that stops the program after printing **stack trace** to the standard error.
[](https://mermaid-js.github.io/mermaid-live-editor/edit#pako%3AeNp90MEKwjAMBuBXKTkpuhcYIogK7iAIXnuJbdCibSXNhDF9dytTD1PWU_rnIy1pwURLUIK5YEorh0dGr4PKZyNy3RPfiFVR3OeqWqHgARMtjKGUOtQL1exeFGpHnFwSCvLpDuAqbMlHbgZpqSZMaF_haNyJ30cyWsYQyIiLQZlvOeD7Q_uf-UNgCp7Yo7N5a-0r0yAn8qShzKVFPmvQ4ZFdfbUotLZOIkMpXNMUsJa4b4L53DvzXnwXPp7k6Ymb)
```
Exception in thread "main" java.lang.RuntimeException: We cannot continue
    at exceptions$package$.attemptSomething(exceptions.scala:6)
    at exceptions$package$.run(exceptions.scala:10)
    at run.main(exceptions.scala:9)
```

Stack traces show the call chain, starting from the position where the error was thrown. Note that the JVM prefixes the methods name with tis class name. This can lead to synthetic names such as `exceptions$package$` for top-level definitions or object definitions.

Exceptions can be caught with a `try/catch` handler:

```scala
@main def run(): Unit =
    try
        attemptSomething
    catch
        case exn: RuntimeException =>
            System.err.println(s"Something went wrong: $exn")
            println("Stopping the program")
```

The syntax for catching exceptions is similar to pattern matching:

```scala
try
    attemptSomething()
catch
    case exn: ArithmeticException =>
        println("An aritmetic exception ocurred.")
    case exn: RuntimeException =>
        println("Something unexpeccted happened")
```

Exception classes are defined in the standard library and form a type hierarchy. Each patter of the `catch` clause check whether the exception that happened is of some specific type or not. `catch` clauses do not have to be exhaustive.

Below we share the `Throwable` hierarchy:

```mermaid
classDiagram
    IThrowable <|-- IError
    IThrowable <|-- IException
    IError <|-- IOutOfMemoryError
    IError <|-- IStackoverflowError
    IException <|-- IOException
    IException <|-- IArithmeticException
```

There are some types of "throwables" that your programs should never try to catch, such as `OutOfMemoryError`. These are known as fatal exceptions.

A `try/catch` block is an expression that return a value:

```scala
val stream = getClass.getResourceAsStream("data.txt")
val data =
    try
        parseIntData(stream)
    catch
        case NonFatal(exn) => -1
    finally
        stream.close()
```

Those expressions can be followed by a `finally` clause that is executed both if no exceptions were thrown or if an exception was thrown.

In summary, exceptions are a low-level mechanism for dealing with unexpected problems during the program execution. Throwing an exception interrupt the program execution unless the exception is cooked by a handler.

### Error Handling with Try
### Manipulating Try Values
### Validating Data
### Manipulating Validated Values
### Combining Try and Eithre

## Asynchronous Programming
### Concurrent Programming
### Operations on Type Future
### Examples with Future
### Execution Context

## Assestment

Wikigraph, please check the contents in `exercises/wikigraph`