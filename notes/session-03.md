# 🚧 Session 3: Build tools, developer workflow, modules, encapsulation

How to write Scala projects larger than a worksheet? Learn how to define an entry point for a Scala project, and discover the tools you can leverage to write, compile, run, and debug your code. Then, use object-oriented programming techniques to break down a complex project into small modular components. Finally, we discuss when to use case classes versus simple classes.

#### Learning Objectives
- Compile, run, and test your code with a build tool
- Break down the components of your program into modules
## 🚧  Tooling

### 🚧  Organize Code

In summary, large Scala projects are split into multiple files. Source files contain definitions organized into **packages**. You can impor entities to not have to write their fullu qualified name every times.

### Build Tools

As a developer whe you work on a program you need:

- compile
- run
- test
- deploy

Some of these tasks can be performed by a continuos integration server, or, may be automatically triggered on source changes.

So, an important question is what is to compile a program? The answer in the context of Scala is invoking the compiler on all the source files to turn them into executable JVM bytecode. this process require:

- constructing the application classpath by fetching library dependencies,
- generating parts of the source code or resources (e.g., assets, data, type serializers, etc.)

Building an execution environment (i.e, a JVM with the correct classpath), invoking the entry points, requires compiling the program.

Also, execute tast like:

- publish an artifact on a library repository,
- package the program and its dependencies as a single `.jar` file,
- publish a Docker image
- publish documentacion
- ...

requires compiling the program.

Invoke manually these tasks (compiling, fetching dependencies, etc.) in the right order is cumbersome. Instead **build tools** can manage these task for you.

Idea: you indicate where you program source files are located, which libraries your program depends on, and the build tool takes care of feteching the dependencies, invoking the compiler, running the test, and more.

Generally, build tools are not limited to a specific developer workflow but implement a generic task engine able to coordinate the execution of a graph of tasks.

```mermaid
graph TD;
    A[fetch] --> B[compile];
    B --> C[run];
    B --> D[deploy];
```

In summary, working oin program involves performin various independent ta sks such as compiling, running, and deploying the program. Build tools aim at simplifyin the coordination of these tasks.
### Introduction to `sbt`

`sbt` is a build tool commonly used in Scala.

> Note: there are other build tools that support Scala: Maven, Gradle, Mill, etc.

Make sure the sbt command-line tool is installed accordin to the instruction in the [sbt's oficial site](https://scala-sbt.org)

As sbt project is a directory with the following two files:

- `project/build.properties`: defines the version of sbt we want to use
- `built.sbt`: defines the configuration of the project

By default, `sbt` compiles source files that are in the directory `src/main/scala`.

When your project contains multiple Scala files, if you modify only one file, `sbt` will try to recompile only this file and the files that depend on it, but not more. This is called *incremental computation*.

To compile you project, first enter to the `sbt` cli of the project running the next command:

```
sbt
```

The out of this command should be:

```
sbt:hello-sbt>
```

In this case `hello-sbt` is the project's name. So to compile the project execute the next command:

```
sbt:hello-sbt> compile
```

Here a `/target` folder should be created as sibling of the `/project` directory. This folder is the output of the compilation an it is cached to agilize the re-compile process. 

The `run` task compiles and then runs the program:

```
sbt:hello-sbt> run
```

Another useful task is `console`, which compiles he program and then opens a Read-Eval Print Loop (REPL), an interactive prompt to evaluate expressions in your program.

Below its recopile in a graph the main concepts and their dependencies:

```mermaid
graph TD;
    A[run] --> C[compile];
    B[publish] --> C;
    C --> D[update];
    C --> E(sourceDir);
    D --> F(libraryDependencies);
```

There are two main conceps int `sbt`:

- **Settings**: Parametrize the build, ther are evaluated one (e.g., sourceDir, libraryDependecies)
- **Tasks**: Perform action (download, compile, run, etc.), they are evaluated at each invocation.
  
Tasks can be parametrized by setting.

Speaking of *library dependencies*, let's see how to use a library for colorizing the text we print to the console. First, we need to declare the dependency in the build definition by appending the so-called coordinates of the library to the `libraryDependencies` key. So to find the coordinates of the library, I'm going to use the [Scala Index](https://index.scala-lang.org). The library I want to use is named [Fansi](https://index.scala-lang.org/com-lihaoyi/fansi). Now we update the `build.sbt` file with the next content

```
// build.sbt
scalaVersion := "3.0.0-RC1"
libraryDependencies += "com.lihaoyi" %% "fansi" % "0.3.0"
```

To ensure the inclusion of the librery we should re-build the project. So, lets `run` the program:

```
sbt:hello-sbt> run
```
and then let's `reload` it.

```
sbt:hello-sbt> reload
```

Now to use the library, lets update the `HelloSbt.scala` file with the next content:

```scala
package hellosbt

val greeting =  "Hello, sbt!!!"

@main def run(): Unit = println(fansi.Color.Red(greeting))
```

After this change when we `run` the program, we get the `"Hello, sbt!!!"` message in red.

This way is how we include library dependencies in Scala via `sbt`

`sbt` provides common predefined task and settings out of the box. Additional task or predefined configurations can be provided by **plugins**. Plugins define how the build itself is managed and must be declared in the `project/plugins.sbt`

In summary, `sbt` is a build tool for Scala. It is interactive: you start the sbt shell in the morning and manage your project from there. The build definition is written in Scala. A build definition essentially assigns values to setting keys (such as `scalaVersion`, or `libraryDependencies`)

### sbt, Keys and Scopes

Let's understand the Scopes concpet in `sbt` and how use them.

We have seen that the source directories are different for the program and its tests. However, there is a single key `sourceDirectory` that allow us to identify the source directory of the project. It is important to know that a key can have different values in diferent **scopes**. Lets illustrate this with the next outputs in the terminal:

```
sbt: hello-sbt> Compile / sourceDirectory
src/main

sbt: hello-sbt> Test / sourceDirectory
src/test
```

There is a single concept of source directory, modeled by the key `sourceDirectory`, reused by both `Compile` and `Test` configurations by scoping the key to the corresonding configuration.

By scoping the key to the corresponding configuration, each key can be assigned a value along a configuration such as `Compile`, `Test` or no specific configuration which is named `Zero`. 

When we look up the value of a key, we can specify the configuration we are interested. If no configuration is specified, `sbt` first tries with the `Compile` configuration and falls back to the `Test` configuration. For instance, just `run` is equivalent to `Compile / run`, which means run in the Compile configuration.

Conversely, if we look up for `Compile / scalaVersion`, which means the value of the setting `scalaVersion` in the scope of the Compile configuration. The key `scalaVersion` has no value in that scope. Then `sbt` falls back to a more generic scope. It looks up in the `Zero` configuration.

Configurations are just one possible axis of key scoping.

Keys can also have different values according to a particular **task** key. For instance, the task `unmanagedSources` lists all the projects source files:

```4yy
sbt: hello-sbt> show unmanagedSourcse
[info] * src/main/scala/hellowsbt/Main.scala
```

The task can be configured by changing the value of the setting `includeFilter`:

```
sbt: hello-sbt> show unmanagedSourcse / includeFilter
[info] ExtensionFilter(java, scala)
```

By default, `sbt` looks for source files with extension `.java` and `.scala`. Let's include `.sc` files in the unmanaged sources updating the `build.sbt` file:

```
// built.sbt
unmanagedSourcse / includeFilter := new io.ExtensionFilter(
    "java",
    "scala",
    "sc"
)
```

and then, when we launcg the query we get:

```
sbt: hello-sbt> show unmanagedSourcse / includeFilter
[info] ExtensionFilter(java, scala, sc)
```
There is a third axis that can be used to assign values to sbt keys.

When a project contains sub-projects, each sub-project can set its own values for some keys. This is typically the case for the setting `baseDirectory`, which defines the root directory of each sub-project.

In our build definition example, we only have one project, so all our settings are scoped to this project. We can explicitly see that by prefixing the name of a key with the name of our project, `hello-sbt`. 

```
sbt: hello-sbt> hello-sbt / sourceDirectory
[info] src
```

There is also a special project named `ThisBuild`, which means the "entire build". so a setting applies to the entire build rather than just a single project.

`sbt` falls back to `ThisBuild` when you look for the value of a key that has not been defined for a specific project.

This is a convenient way to define cross-project settings

```
// Set the Scala version for all the projects in this build definition
ThisBuild / scalaVersion := "3.0.0"
```

Here is how we can see the value of the `includeFilter` key according multiple axes:

```
// current project, no configuration, unmanagedSource task
unmanagedSources / includeFilter

// hello-sbt project, no configuration, unmanagedSource task
hello-sbt / unmanagedSources / includeFilter 

// hello-sbt project, Compile configuration, unmanagedSource task
hello-sbt / Compile/ unmanagedSources / includeFilter 
```

In summary, when the same concept (e.g., a source directory) is reused in several context such as configurations (e.g., the program and its test), or tasks, sbt encourages you to use a single setting key for this concept and to scope the value you assign to it to the desired context.

### Program Entry Point

Unlike worksheets that are evaluated from top to bottom, Scala projects hava a program entry point.

A program entry poin is a mehtods definitions annotated with `@main`,

Program entry points can take parameters.

Project source files cannot contain top-level statements, only top-level definitions. Those top-level definitions are `def`, `val`, `var`, `object`, `trait`, and `class` definitions.

## Modules

Consider the following situation. We want to implement a program that exposes data read from the database as JSON documents.

The part of the program that exposes the data calls the part of the program that reads from the database, and it serializes the result into JSON.

The *problem* we want to avoid is that if the database connection is widely available to any part of the program, it might be too easy to mess up with the state of the system. Instead, we want to restrict access to the database connection, so that we can increase our confidence that the code that uses it is correct. 

This leads to the following architecture:

```mermaid
classDiagram
    HttpServer --|> DatabaseAccess
    DatabaseAccess : +connection Connection
    DatabaseAccess: +readData() List[Data]
```

The HTTP server cannot access the underlying `connection` of the `DatabaseAccess` module. It can only use the public `readData()` operations.

Translate the diagram into Scala code should throw the next definitions:

```scala
class DatabaseAccess(connection: Connection):
    def readData(): List[Data] = ...

class HttpServer(databaseAccess: DatabaseAccess):
    ...
    databaseAccess.readData()
    ...

def main() =
    val connection: Connection = ...
    val databaseAccess: DatabaseAccess = DatabaseAccess(connection)
    val httpServer: HttpServer = HttpServer(databaseAccess)
    ...
```

Let's go deep in the next lines fo code:

```scala
class DatabaseAccess(connection: Connection):
    def readData(): List[Data] = ...
        connection
        ...
```

This code defines a **type** `DatabaseAccess`and a **constructor** of the same name. The type `DatabaseAccess` has one method, `readData`.

By contrast with case classes, constructor parameters of "simple" classes area **private**. i.e., they can be accessed only from the class body.

This highlights one difference between case classes and simple classes: the former achieve aggregation whereas the latter achieve **encapsulation**. Let's go deep with encapsulation in the next section.
### Encapsulation

Class members are public by default: a user of the class `DatabaseAccess` can call its `readData` operaiton. It is also posible to define private members by qualifying them as such:

```scala
class DatabaseAccess(connection: Connection):
    private def decodeTableRow(row: TableRow): Data = ...
    def readData(): List[Data] = ...
```

The operation `decodeTableRow` can only be called from the insde of the class `DatabaseAccess`.

Sometimes, is useful have several implementations of the same type. For instance is a common practice to use a persistent database for production, but an in-memory database for some tests.

```mermaid
classDiagram
    DatabaseAccess <|-- PersistentDatabase
    DatabaseAccess <|-- InMemoryDatabase
    DatabaseAccess: +readData() List[Data]
    PersistentDatabase : +connection Connection
    class PersistentDatabase {
        readData(connection) List[Data]
    }
    class InMemoryDatabase {
        readData() List[Data]
    }
```

You can define an interface in Scala by writing a `trait` definition.

```scala
trait DatabaseAccess:
    def readData(): List[Data]

class PersistentDatabase(connection: Connection) extends DatabaseAccess:
    def readData(): List[Data] = ...

class InMemoryDatabase extends DatabaseAccess:
    def readData(): List[Data] = ...
```


Again, let's check the next lines of code:

```scala
trait DatabaseAccess:
    def readData(): List[Data]
```

This code defines a **type** `DatabaseAccess` but **no constructor**. The type `DatabaseAccess` has one **abstract method**, `readData`.

Unlike sealed traits, "simple" traits can have an unbounded number of implementations.

Classes that extend the trait `DatabaseAccess` have to implement the method `readData`, otherwise, it will throw a compile error.

> Note: It is good practice to depend on interfaces rather than specific implementations. 

Below, it is recopile the visibility class/trait members:

- **public** members are visible from the outside of a class or trait, 
- **private** members are visible from the inside of class or trait, and there is a third level of visibility.
- **protected** members are visible from the inside of a trait or class and from the inside of its descendant (but not from the outside like private members).

Let's check the protected members with an example. With a combinatin of protected and abstract members we can make a dish while delaying to a later point the secret sauce brought by a chef:

```scala
trait Chef:
    protected def secretSauce(sauce: Sauce): Sauce
    def makeDish(meat: Meat, vegetables: Vegetables: Dish) =
        ...
        sercretSauce(...)
        ...

object YukihiraSoma extends Chef:
    protected def secretSauce(sauce: Sauce): Sauce = ...
```

In summary, define abstraction barriers with classes and traits, which encapsulate implementation details.

Use private members to restrict the visibility of members to the inside of class or trait definition, and use abstract members to delay the implementation of operation to concrete classes.

### Extending and Refining Classes

In this section let's check the feature of Scala to do object-oriented programming.

Traits and classes can form a rich hierarchy. An example is the standard collections.

```mermaid
classDiagram
    Iterable <|-- Set
    Iterable <|-- Seq
    Iterable <|-- Map
    Seq <|-- List
    Seq <|-- Vector
```

Each subtype may introduce more specific methods. For instance, the type `Seq` introduces methods for sorting elements. This operation does not exist on `Set` and `Map` because by definition their elements have no order.

Actually, each collection type comes in two variant, immutable and mutable.

Scala has the next class hierarchy:


```mermaid
graph TD
    B[scala.AnyVal] --> A[scala.Any];
    C[scala.AnyRef] --> A[scala.Any];
    D[scala.Unit] --> B
    E[scala.Boolean] --> B
    F[scala.Char] --> B
    G[scala.Byte] --> B
    H[scala.Double] --> B
    I[scala.String] --> C
    J[scala.Itereable] --> C
```

At the top of the type hierarchy we find:
    - `Any` the base type of all types; methods `==`, `!=`, `equals`, `hashCode`, `toString`
    - `AnyRef` The base type of all reference types; alias for `java.lang.Object`
    - `AnyVal` The base type of all primitive types.

A clas or object definition can extend **several** traits:

```scala
trait Logging:
    val logger = ...

class InMemoryDatabase extends DatabaseAccess, Logging:
    def readData(): List[Data] =
    ...
    logger.debug("...")
```

Also you can **override** the implementation of inherit members. For prevent a member from being overridden you can qualifying it with `final`.

Last, within a class definition, you can use the identifier `this` to refer to the instance being defined.

We have introduced traits with abstract methods as a way to define interfaces, and classes that extend these traits as a way to provide concrete implementations. Scala supports other variations around traits and classes, such as abstract classes, traits parameters, or secondary constructors. These constructs have subtle differences or specificities, which are out of the scope of this post.

In summary, types form a hierarchy whose top type is `Any`.

Multiple traits can be mixed to a class or an object definition.

From within a class definition, you can refer to:

- The instance being defined with the identifier `this`
- the parent implementation of a methods `m` with `super.m`

You can override inherited members, unless they are final.

### Case Classes vs Simple Classes

In this section it will discuss the differences between **case classes**, and **simple clases**. In the same way, it will abord the difference between **sealed traits** and **simple traits**.

We have seen how to use classes and tratis to achieve encapsulation. We have previously been using case classes and sealed traits to model bussines domains.

As we mentioned in the previously, the main difference between case classes, and simple classes is that case classes achieve **aggregation**, whereas simple cases achieve **encapsulation**.

An important consequence is that constructor parameters of case classes are promoted to **public** members, whereas constructor parameters of simple classes are by default **private**.

 Although the purpose of aggregation, and encapsulation are quite different, it makes sense to model both with the same concept, namely classes. Actually, a case class is just a special case of a simple class

When you define a case class, the compiler defines a class, and customizes some parts of it:

- constructor parameters are promoted to public members (a.k.a. **fields**),
- an **extractor** enables pattern matching,
- **equality** operator between instances compares the values of the case class fields.

The equality part is quite important, lets check an example to go deep with this feature. If we got the next definition:

```scala
case class CaseClass(value: Int)

CaseClass(42) == CaseClass(42) // true
```

However, with default equality:

```scala
case class SimpleClass(value: Int)

SimpleClass(42) == SimpleClass(42) // false
```

We will discuss this diference later.

Despite these differences, both case classes and simple classes: 

- define a new type, along with the new constructor.,
- can have public and private members,
- can extend traits and override members, and 
- create abstraction levels

Now let's see the difference between sealed traits and simple traits. The only difference between sealed traits, and simple traits is that sealed traits have a fixed number of sub classes. Whereas simple tratis can have an unlimited number of subclasses.

More precisely, the sub classes of a sealed traits have to be defined in the same file as the substrate itself.

 A practical consequence of that, is that exhaustivitty checking in pattern matching is only possible with sealed traits.

 Despite these differences, both sill traits and simple traits:

 - define a new type with no constructor,
 - can have concrete and abstract members,
 - can have public protected, and private members, and
 - create abstraction levels.

Case classes and seed traits make you think of types as **sets of possible values**. For instance, the type `Boolean` has two possible values, `true` and `false`, the type `Option[Boolean]` has three possible values and so on.

Alternatively, "simple" traits and classes make you think of types as **interfaces** that provide a specific set of operations, for instance, the type `Boolean` has logic operation such as `||` and `&&`.

In summary, types can be seen as sets of operations (with an unbounded number of possible values), or sets of possible values (with an unbounded number of operations).

Classes conveniently group operations together.

If the set of possible values of the type is bounded, then it is probably a good idea to model this type with a case class. Conversely, if the set of possible operations, and the values of some type is bounded, then it is probably a good idea to model this type with a simple class.

### Opaque Types

Let's check how to define zero cost abstractions with opaque type aliases.

Consider the following API for manipulating users and vehicles

```scala
def findVehicle(vehicleID: Long): Option[Vehicle] = ...
def mistake(userId: Long): Unit =
    findVehicle(userID) // What?
```

Problem: since both vehicle IDs and user IDs are modeled with type `Long`, we can mix them!

One way to prevent this problem is to use distinct types for representing user IDs and vehicle IDs:

```scala
case class UserID private (value: Long)

object UserID:
    def parse(string: String): Option[UserID] =
        string.toLongOption.map(id => UserID(id))
```

- This code defines a public type UserID, but its constructor is private (it can be accessed from within the UserID companion object, though)
- The only way to construct a value of type UserID is to use the operation UsertID.parse

Another approach could be:

```scala
def findVehicle(vehicleID: VehicleID): Option[Vehicle] = ...
def mistake(userId: UserId): Unit =
    findVehicle(userID) // What?
    //          ˆˆˆˆ
    //          Found (UserID: UserID), Required: VehicleID
```

This nonsesical program is now rejected. However, the `UserID` and `VehicleID` abstractions have a cost: they instantiate a wrapper object.

To remove the runtime overhead we can use a **type alias**:

```scala
type UserID = Long

object UserID:
    def parse(string: String): Option[UserID] =
        string.toLongOption // Option[Long]
```

Type aliases incur no runtime costs (there is no class added), and can be interchageably used with the type they are an alias to. They are alos handy to provide a shorthand name for complex type expression. However, with simple type aliases we are back to square one...


```scala
type UserID = Long
type VehicleID = Long

def findVehicle(vehicleID: VehicleID): Option[Vehicle] = ...
def mistake(userId: UserId): Unit =
    findVehicle(userID) // What?
```

**Opaque** types offer the best of both worlds:

```scala
object UserID:
    opaque type UserID = Long

object VehicleID:
    opaque type vehicleID = Long

import UserID.UserID
import VehicleID.VehicleID

def findVehicle(vehicleID: VehicleID): Option[Vehicle] = ...
def mistake(userId: UserId): Unit =
    findVehicle(userID) // What?
    //          ˆˆˆˆ
    //          Found (UserID: UserID.UserID), Required: VehicleID.VehicleID
```

Below some specific features of opaque type aliases:

- Like type aliases, opaque type aliases incur no runtime overhead,
- Inside the scope of the alias definition, the alias is transparent:

```scala
object UserID:
    opaque type UserID = Long
    // Within the object ID, the type User can be used
    // interchageablu with the type Long

end UserID
```

- Outside its scope, the alias is "opaque": it hides the type it is an alias to.

By definition, there is nothing one can do with a value of type `UserID`. The object that defines the opaque type alias should also defined methods that *produce* and *consume* opaque types:

```scala
object UserID:
    opaque type UserID = Long
    def parse(string: String): Option[UserID] = String.toLongOption
    def value(userID: UserID): Long = userID
```

In summary, type aliases let you give a name to a type expression and opaque type aliases encapsulate the type they are an alias to. Opaque type aliases are transparent inside the scope they are defined. But they are opaque outside of it, so you can define zero cost abstractions with opaque types.
### Extensions Methods

Let's see how to add new methods to an existing type.

In the previous section, you have seen why opaque types have no methods, so, you have to define auxiliary methods to work with opaque types. 

```scala
object UserID
    opaque type UserID = Long
    def value(userID: UserID): Long = userID
```

For instance, to receive the underlying `Long` value of a user ID you would write"

```scala
UserID.value(userID)
```

Instead, it would be more practical to just write:

```scala
userID.value
```

We can define a method `value` to the type `UserID` by writing an **extension method**.

```scala
object UserID
    opaque type UserID = Long
    extension (userID: UserID)
        def value: Long = userID

import UserID.UserID

def findUser(userID: UserID): Option[User] =
    ...
    userID.value
    ...
```

Extension methods aer not specific to opaque types. In fact, you can use this mechanism to enrich any type with additional methods.

When you define an extension method, the compiler translate the extension methods to regular methods. As consequence, extension methods can also be called with a syntax `(operation)(firstParam)(secondParam)`.

Since the type `userID` has no actual method `value` and the type `Int` has no actual method `isZero`. How does the compiler resolve codes to extension methods?

What happens is that when you call a method `m` on an expression `e` of type capital `E`, like in `e.m`. and that the type `E` has no method `m`. The compiler will try to rewrite the call into `m(e)`.

So this is what happens when we write `42.isZero`. This expression does not type check as is. So the compiler tries `isZero` of 42 which does type check. Note that `isZero` of`42 does type check only if the extension method `isZero` is visible at the point where the call is performed. Let's illustrate what it means to be visible with some examples.

```scala
trait IntExtensions:
    extension (n: Int) def isZero: Boolean = n == 0

    0.isZero // OK, isZero is defined in the enclosing scope

object IntExtensions extends IntExtensions
    1.isZero // OK, isZero is defined in the enclosing scope

2.isZero // Error: value isZero is not membe of Int

import IntExtensions.isZero
3.isZero // OK, isZero is imported
```

Since the compiler looks for extension methods only when the method that is called is missing. It means that extensions can only add new members, but not overwrite existing ones. Also, extensions cannot refer to other class members via `this`.

Consider again the following program:

```scala
object UserID
    opaque type UserID = Long
    extension (userID: UserID)
        def value: Long = userID

import UserID.UserID

def findUser(userID: UserID): Option[User] =
    ...
    userID.value
    ...
```

Here the extension method `value` is not visible at the point where the call `userID.value` is performed, in the method `findUser`.

Why do not we need to import `UserID.value`?

In this case, we don't need to import the extension method, because there is an additional rule applied by the compiler. Since the type `userID` is an opaque type. The compiler also looks for extension methods in its scope of definition.

In summary, extension methods let you introduce new methods to existing types. The compiler allows you to apply extension methods, if they are visible at the point of application. Which means if they are defined in an including scope, inherited or imported. In the case of opaque types, the compiler also looks for extension methods in the scope of definition of the opaque type.

## Assestment

Todo List, please check the contents is in `exercises/todo-list`.