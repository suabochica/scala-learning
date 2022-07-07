# 🚧 Session 3: Build tools, developer workflow, modules, encapsulation

## 🚧  Tooling

### 🚧  Organize Code

In summary, large Scala projects are split into multiple files. Source files contain definitions organized into **packages**. You can impor entities to not have to write their fullu qualified name every times.

### ✅ Build Tools

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
### 🚧 Introduction to `sbt`

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

### 🛑 sbt, Keys and Scopes

## 🛑 Modules

### 🛑 Encapsulation

### 🛑 Extending and Refining Classes

### 🛑 Case Classes vs Simple Classes

### 🛑 Opaque Types

### 🛑 Extensions Methods

// ✅  🚧  🛑