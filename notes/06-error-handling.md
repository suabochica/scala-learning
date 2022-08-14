
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

Let's review a high level variant of `try/catch` handler, which also provides the benefits of checked exceptions because it explicitly signals the possibility of a failure in type signatures.

We have seen that runing a program may be interrupted in case something unexpected happens an the program throws an exceptions. What should we do about that? In some cases, aborting the program execution is what we want, so there is no need to handle exceptions. However in most cases, we do not want to abort the program execution: Servers need to return a response, long-running programs need to keep running, etc.

In case we need to handle exceptions, shoudl we add `try/catch` handlers around each expression? This *defensive* way of programming would be very inconvenient. One way to address this problem is to explicity model the fact that our program can fail. In the next sections we will introudce different techniques to achieve this. For now, lets check some guides to explicit modeling of failures.

- If exception sare used as a last resort only, there is no need to be defensive, unless we call an API that we know uses exceptions (e.g., some low-level APIS)
- Just define an exception handler at the beginning og your program, or use the default exceptions handler provided by the runtime.
- Explicitly indicate in the result type of a method that it can either succeed or fail.

You can explicitly indicate that a method may fail by returning a value of type `Try`:

```scala
import scala.util.Try

def attemptSomething(): Try[Unit] =
    Try {
        println("So far, so good")
        println("Still there")
        throw Runtime Exception("We cannot continue")
        println("You will never see this")
    }
```

The type `Try[A]` inform users that the program may fail. A value of type `Try[A]` can either be a `Success[A]`, or a `Failure`.  If an exception is thrown withing a `Try {...}` block, we get a `Failure`, othrewise we get a `Success` containing the result of evaluating the block.

At the use site, onew way to handle errors is by calling the `recover` error method:

```scala
@main def run(): Unit =
    attemptSomething
        .recover {
            case exn: RuntimeException =>
                System.err.println(s"Something went wrong: $exn")
                println("Stopping the program")
        }
```

So, we use recover like we would use `catch` in a `try` expression.

Partial functions are functions that may not be defined on all their domain type. For instance, a `PartialFunction[Int, String]` is a function that may not be defined fo some `Int` values. Before applying a partial function to a value, you should check that it is defined for this value by calling `isDefinedAt`:

```scala
if somePartialFunction.isDeinedAt(someValue) then
    somePartialFunction(someValue)
```

In the previous example, the return type of `attemptSomething`, was `Try[Unit]`, which means that this program may fail but does not retunr a meaning.

Alternatively, consider a program

```scala
import java.time.LocalDate

def parsseDate(str: String): Tru[LocalDate]
```

which may fail to return a `LocalDate`. You van use it as follows:

```scala
import scala.util.{Failure, Success}

parseDate("2022-02-02") match
    case Success(date) => println("Successfully parsed date")
    case Failure(throwable) => println("Failed to parse date")
```

Returning a value of type `Try[A]`, as opposed to returning a value of type `A` is a double edged sword:

- On the one hand, callers of your program won´t be surprised by a possible failure,
- On the other hand, they will have to deal with failures; the cannot just ignore them an let the program be aborted.


Explicit modeling of failures may not always be relevant. It is robably a good choice, though, if the failure is likely to happen (e.g., parsing data, openning file, performing remotes calls).

A typical use case is to wrap a Java API that models failures by throwing exceptions.

In summary, the type try makes it explicit that the computation may fail, and it lets you manipulate, successful, result or recover from exceptions. The value of type try can be either a success or failure. It is common practice to use try blocks to wrap calls to API that model failure by throwing exceptions.

### Manipulating Try Values
Lets review the most common high-level operations to use to manipulate values of type `Try`.

For instance, consider a situation where you want to read two dates from `String` values, and compute the duration between theme. Parsing a date is an operation that may fail, but for this case we do not to deal with the failures yet, lets to focus on the case where both dates were successfully parsed. Please check the contents in `examples/week_6/src/main/scala/org/epfl/03_try/parsingDates.worksheet.sc`

```scala
import java.time.LocalDate
import java.time.Period
import scala.io.Source
import scala.util.{Success, Try, Using}

def parseDate(str: String): Try[LocalDate] =
  Try(LocalDate.parse(str))

def tryPeriod(str1: String, str2: String): Try[Period] =
  parseDate(str1).flatMap { date1 =>
    parseDate(str2).map { date2 =>
      Period.between(date1, date2)
    }
  }

tryPeriod("2020-07-27", "2020-12-25") // : Try[Period] Success
tryPeriod("2020-19-27", "2020-12-25") // : Try[Period] Failure
```

Alternatively, we can use the `for comprehesion` in the body of the function `tryPeriod` that is a syntactic sugar for nested flatMaps.

```scala
def tryPeriod(str1: String, str2: String): Try[Period] =
    for
        date1 <- parseDate(str1)
        date2 <- parseDate(str2)
    yield
        Period.between(date1, date2)
```

The result is the same.

Here is a summary of the transformation operations on the type Try.

```scala
trait Try[A];
    def map[B](f: A => B): Try[B]
    def flatMap[B](f: A => Try[B]): Try[B]
    def recover(f: PartialFunction[Throwable, A]): Try[A]
    def recoverWith(f: PartialFunction[Throwable, Try[A]]): Try[A]
end Try
```

Now lets go deep with a harder problem: Instead of parsing just two dates, we lets parse an arbitrary number of dates, read from a file. Each date is written in a new line:

```
1970-01-01
2004-01-20
2016-07-15
```

A first approach could be:

```scala

def readDateStrings(fileName: String): Try[Seq[String]] = Try {
    val source = Source.fromFile(fileName)
    val dateStrings = source.getLines.toSeq

    source.close()
    dateStrings
}
  
def parseDates(fileName: String): Try[Seq[LocalDate]] =
  readDateStrings(fileName).flatMap { dateStrings =>
    dateStrings.foldLeft[Try[Seq[LocalDate]]](Success(Vector.empty[LocalDate])) {
      case (tryDates, dateString) =>
          for
              dates <- tryDates
              date <- parseDate(dateString)
        yield
            dates :+ date
    }
  }

parseDates("./src/main/scala/org/epfl/03_try/dates-file.txt")
```
Here we can rewrite the `readDateStrings` function with `Using` to mx in resource acquisition and release with operations that can fail:

```scala
def readDateStrings(fileName: String): Try[Seq[String]] =
  Using(Source.fromFile(fileName)) { source =>
    source.getLines().toSeq
  }

```

In summary, you should leverage the operations `map` and `flatMap` to manipulate Try values while postponing failure handling to a later point in the program. And you should also leverage `Using` to mix in resource acquisition and release with operations that can fail.

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