# Session 2: Generic Data Types

## First steps with Lists

We will see an overview of the `List` data type: how it can be used to model collections of values, how to construct `List` instances, and some basec manipulation operations.

Let's use an example of use of collections to model an adress book.

> And address book contains several contacts
> A contact has a name, an email, and possibly several phone numbers.

```scala
case class AddressBook(contacts: List[Contact])
case class Contack(
    name: String,
    email: String,
    phoneNumbers: List[String]
)
```

Collection types ara **parametrized** by the type of their elements: for instance, the type `List[Contact]` is the type of a list with elements of type `Contact`, and the type `List[String]` is the type of a list with elements of type `String`.

Consequently, the elements of a list must all have the same type.

With the domain model defined, we can construct and address book with two contacts, Pam and Jim:

```scala
val pam = Contact("Pam", "pam@theoffice.tv", List())
val jim = Contact("Jim", "jim@theoffice.tv", List("+417787829420"))

val addressBook = AddressBook(List(alice, bob))
```

A list having `x1, ...,xn` as elements is written `List(x1, ..., xn)`.

The basic list manipulation are:

- get the size of the list
- check if a list contains an x element
- map the list
- filter the list

```scala
val numberOfContacts: Int = addressBook.contacts.size // 2
val isPamInContacts: Boolean = addressBook.contacts.contains(pam) // true
val contactNames: List[String] = addressBook.contacts.map(contact => contact.name) // List("Pam", "Jim")
val contactsWithPhone: List[Contact] = addressBook.contacts.filter(contact => contact.phoneNumbers.nonEmpty) // List(Contact("Jim", "jim@theoffice.tv", List("+417787829420")))
```

To summary, List are one particular **data structure** modeling a collection of elements.

Lists can be manipulated with high-level operations for transforming or filtering their elements.

## Functions

Functions are very much like methods, they have parameters and they return results. For example, the `map` operation takes as parameter a _function_ applied to each element of the list.

```scala
contacts.map(contact => contact.name)
```

We pass a _function literal_ as a parameter. In this example, the function takes one parameter, named `contact` and returns the expression `contact.name`.

Below we share some examples of fucntions:

```scala
val increment: Int => Int =
    x =>
        val result = x + 1
        result

val add =
    (x: Int, y: Int) => x + y

add(1, increment(2)) // 4
```

More generally, a value like

```scala
(t1, t2, ..., tn) => e
```

Is a function that take `n` parameters, and which returns the expressions `e`.

In case the compiler cannot infer the types of the function arguments, you have to write them explicitly:

```scala
(t1: T1, t2: T2, ..., tn: Tn) => e
```

Last, a function is a value, so it has a _function type_. The syntax for function type looks like the syntax for function literals:

```scala
(t1: T1, t2: T2, ..., tn: Tn) => e
```

So let's go ahead with the next puzzle: what is the type of the following function?

```scala
(contact: Contact) => contact.email.endsWith(@sca.la)
```

The anwser is `Contact => Boolean`.

Now, what is the difference between the following definitions of `increment`?

```scala
def increment(x: Int): Int = x + 1 // Method
val increment: Int => Int = x => x + 1 // Function
```

In both cases, we can call the `increment` like the following:

```scala
increment(41) // 42
```

The difference is a bit technical, and come from the fact that Scala supports both; functional programming and object oriented programming.

A key difference is that the second version defines a **value** that can be passes as a parameter or returned as a result.

The runtime creates an object for it in memory.

Calling a function means calling its method `apply`:

```scala
increment(41) // 42
// is equivalent to
increment.apply(41)
```

When we write `increment(41)`, the compiler rewrites it to `increment.apply(41)`.

Most of the time, you do not need to think about wheter you use a method or a function, because the compiler is able to automatically convert methods into functions when necessary.

for instance, you can write:

```scala
val xs: List[Int] = List(1, 2, 3)
def increment(x: Int): Int = x + 1
xs.map(increment) // = List(2, 3, 4)
```

Here, the compiler sees that the operation `map` expects a function, so it converts the method `increment` into a function value.

In summary, function van be used as values.

You can define a function with the arrow syntax: `(x: Int)=> x + 1`.

A function that takes a parameter of type `A` and returns a result of type `B` is a value of type `A => B`, whihc has an `apply` method:

```scala
def apply(a: A): B
```

Calling the function means calling its `apply` method.

### Short syntax for function

Functions whose body use their arguments exactly once do not need to name them. Instead, you can use a placeholder represented by the character underscore `_`.

For instance:

```scala
val increment: Int => Int = x => x + 1
// is equivalent to
val increment = (_: Int) + 1
```

The placeholder syntax also works with multiple parameters:

```scala
val add: (Int, Int) => Int = (x1, x2) => x1 + x2
// is equivalent to
val add: (Int, Int) => Int = _ + _
```

However, there is a warning because the underscore `_` is also used to idicate a wildcard argument. Wildcard argument should not be confused with the placeholder syntax. The difference between them is that wilcard arguments are followed by an arrow `=>`:

```scala
val placeholder = (_: Int) + 1
val wildcard = (_: Int) => 42
```

## More Operations on List

In the previous section we have seen how to construct lists and how to do some basic operations with them.

In this section we will cover:

- how to insert an element into a list
- how to access to one particular element of a list
- what is the data layout of lists
- how the data layout impact the way we work with lists

A `List` is:

- the empty list `Nil` or
- a pair containing a head element and a tail that is a `List`

The lists in Scalara are **immutable**: once you create a list, you cannot change its elements.

If you want a different list, you have to create another one. Let's see how to work with them, considering the next list of contact with Pam and Jim.


```scala
val contacts1 = List(pam, jim)
```

We can create another list wiht Michael, Pam, and Jim like so:

```scala
val contacts2 = michael :: contacts1
```

We have not changed the list `contacts1`. Instead, we have constructed a new list, `contacts2`, whose head element is `carol` and whose tail list is `contacts1`.

Constructing a new list by prepending an element to an existing list is a constant-time operation (i.e. we do not copy the tail list, we just **reuse** it).

This type of data structures is also called **persistent data structures** because we never change the previous state of a list.

Another perspective is that calling the `List` constructor with several elements is equivalent to prepending these elements to the empty list `Nil`:

```scala
List(pam, jim) == pam :: jim :: Nil
```

Note that the `::` operator is **right-associative**:

```scala
pam :: jim :: Nil == (pam :: (jim :: Nil))
pam :: jim :: Nil == Nil == Nil.::(jim).::(pam)
```

More generally, operators that end with `:` are right associative. This contrast with other operator such `+`

```scala
0 + 1 + 2 == ((0 + 1) + 2) == 0.+(1).+(2)
```

Conversely, we can decompose a list into its head and tail by using _pattern matching_.

For instance, here is a program that prints the name of the first contact of an adress book:

```scala
addressBook.contacts match
    case contact :: tail => println(contact.name)
    case Nil => println("No contacts")
```

Decomposing a list into its head an tail is symmetrical to constructing a list from a head and a tail.

```scala
val addressBook = AddressBook(pam :: bob :: Nil)

addressBook.contacts match
    case first :: second :: Nil => println(second.name)
    case _ => println("Unexpected number of contacts")
```

We use the **wildcard pattern**" `_` to mathc an empty list of contacts, a list of exactly one contact, and a list with more than two contacts.

Here is what the compiler would tell us if we wrote `case Nil` instead of `case _`

```scala
addressBook.contacts match
    case first :: second :: Nil => println(second.name)
    case Nil println("Unexpected number of contacts")

// waring: match may not be exhaustive

// It would fail on pattern case List(_, _, _, _, *), List(_)
```

To access the elements of a list we use its operations `head`, `tail`, or random access:

```scala
val fruits = List("apples", "oranges", "pears")

fruits.head == "apples"
fruits.tail == List("oranges", "pears")
fruits.tail.head == "oranges"
fruits(0) == "apples"
fruits(2) == "pears"
```

This operations raise an **error** if you try to access the head or the tail of an empty list, or if you use an index that is out of the bounds of the list.

For other side, the randoms access is not efficient on list.


| Operation     | Complexity    |
| ------------- | ------------- |
| ::            | Constant time |
| head          | Constant time |
| tail          | Constant time |
| random access | Linear time   |
| size          | Linear time   |

To summarize, lists are linear immutable sequences. They can be constructed and decomposed with the `::` operator. They are not optimized for random access but support efficiend `head` and `tail` decomposition.

## Introducing Collections

We have already met the `List` collection. We will now learn about another collection types, In particular how to:

- construct;
- query; and
- transform

collections.

We will learn about specific features of sequence and maps.

We will also ecounter tuples and `Option`, wich are not part of the collection classes but are very usefull.

> Note: the collection library is large. We will only cover the most common cases.

Let's use three different collections types to illustrate commonality and difference between collections:

- `List`, which is an immutable sequence;
- `ArrayBuffer`, which is an mutable sequence;
- `Map`, which is an immutable dictionary.

A dictionary is also known as a _map_, _hash table_, or _associative array_. It is a datastructure that allows us to store and retrieve values by key. For example, we could store user keyed by their user id, and then quickly retrieve a user given an id.

Collections live in the `scala.collection` package:

- `scala.collection.immutable` for the immutable collections; and
- `scala.collection.mutable` for the mutable collections.

By default mos of the immutable collection are already available to use. However we need to impor the mutable collections before we can use them.

A common practice is to import the `mutable` package directly.

```scala
import scala.collection.mutable
```

then we refer to definitions by prefixing them with the packages.

```scala
val buffer = mutable.ArrayBuffer()
```

This convention makes it clear when we are creating a mutable collection.

We started our introduction to collection by looking at three examples of collections types: `List`, `ArrayBuffer`, and `Map` and learned about the packages in which they are found.

## Constructing Collections

In this section we will learn some of the many ways that can be used to construct collections.

The simplest example is an empty collections. Let's construct one.

```scala
List.empty // List[Nothing]
mutable.ArrayBuffer.empty // ArrayBuffer[Nothing]
Map.empty // Map[Nothing, Nothing]
```

Scala inferred the type of the element of our collections as `Nothing`. This is not recomendable, instead we can specify the type:

```scala
List.empty[Int] // List[Int]
mutable.ArrayBuffer.empty[Double] // ArrayBuffer[Double]
Map.empty[String, Boolean] // Map[String, Boolean]
```

Constructing empty collections is not always usefull. There is a constructor to create a cllection with an arbitrary number of elements. We have already seen this for `List`

```scala
List(1, 2, 3, 4)
```

It works for `ArrayBuffer`as well

```scala
mutable.ArrayBuffer("a", "b", "c", "d")
```

This is sometimes known as a _vararg_ constructor, because we can pass a variable number of arguments to it.

To use the vargarg constructor for `Map` we must pass both keys and values.

```scala
Map("a" -> true, "e" -> true, "b" -> false)
```

This creates a map with the key "a" associated with the value `true` and the key "b" associated with the value `false`.

The syntax `a -> b` constructs a _tuple_. Let's take a quick look at tuples before move on.

A typle is a collection of _fixed size_  bu the values in the collection may have _different types_. In contrast collectinos like `List` can hold a variable number of elements but each element must have the same type.

The syntax `a -> b` constructs a tuple with two elements. It is a shorthand for writing (a, b), which is the general syntax for constructing tuples. For tuples of three or more elements we must surround the values wiht parentheses and have commas between them. Here are some examples.

```scala
val pair1: (String, Int) = "Alice" -> 42
val pair2: (String, Int) = ("Alice" -> 42)
val tuple: (Int, String, Boolean() = (42, "foo", true)
```

More generally, a typle type (T1, T2, ..., Tn) is a type containing n elements of type T1, T2, ..., and Tn, respectively.

We now know how to construct types. Let's look at how we get values of them. A pattern mathc is the easiest way to deconstruct a tuple.

```scala
(10.0, "Hello") match
    case (number, greeting) => s"$greeting! the numer is $number"
```

We can use a pattern in `val`, which is another convenient way to desconstruct a tuple.

```scala
val (x, y) = (10.0, 20.0)
x // 10.0
y // 20.0
```
We can also directly access element of a tuple by index. To do so we call a tuple as if it was a function, passing the index of the elements as a parameter. Indices start at 0.

```scala
val pair = ("Alice", 42)
pair(0) // : String = "Alice"
pair(1) // : Int = 42
```

If we already have a sequence we can prepend and append individual element using the `+:` and `:+` methods.

```scala
0 +: List(1, 2, 3) // List(0, 1, 2, 3)
mutable.ArrayBuffer("a", "b") :+ "c"// ArrayBuffer("a", "b", "c")
```

We cannot call `+:` and `:+` on `Map` because it does not store values in any particular order. Instead, we can add an element using the + method.

```scala
Map("a" -> true) + ("e" -> true) // Map(a -> true, e -> true)
```

To recap, we have seen the following methods to construct collections:

- `empty` to construct an empty collection;
- the `varags` contructor;
- `+:` and `:+` to prepend and append elements to sequences; and
- `+` to add an element to a map.

We also learned about tuples.

## Querying Collections

Several methods allow us to query simple properties of a collection, such as the number of elements it contains.

| Operation               | Description                                  |
| ----------------------- | -------------------------------------------- |
| xs.size                 | Number of elements of the collections xs     |
| xs.isEmpty, xs.nonEmpty | Is the collection xs empty, or not?          |
| xs.contains(x)          | Does the collection xs contain the elemen x? |

Sometimes we want to find the first or all the elements in a collection that satisfy a predicate. For this we can use the `find` and `filter` methods respectively

```scala
val data = List(1, 2, 3, 4)

data.find(x => x % 2 == 0) // Some(2)
data.filter(x => x % 2 == 0) // List(2, 4)
```

We will learn all about `Option` soon. For now we need to know that `Options` is a special collection that contains zero or one element. Thus it can represent "optional" value; a value that might be missing.

There are two cases to `Option`:

- `Some` when there is a value; and
- `None` otherwise.

When we call `find` it will return the `Some` case of the `Option` if a value is found.

```scala
List(1, 2, 3, 4).find(x => x == 1) // Some(1)
```

If no value is found the `None` case is returned.

```scala
List(1, 2, 3, 4).find(x => x == 5) // None
```

We have seen the following methods to query properties of a collection

- `size` to get the number of elements in a collections;
- `isEmpty` and `nonEmpty` to determine if a collection contain elements;
- `contains` to test if a collection contains an element;
- `find` to return the first element that matches a predicate;
- `filter` to return all the element that match a predicate;

## Transforming Collections

In this section we will learn about a handful methods that allow us to transform the elements of a collection to produce a new collection, or, in the case of `foldLeft`, anything all.

Using `map` we can transform every element into a new element. The new element can have the same or different type.

```scala
val list = List(1, 2, 3, 4)
list.map(x => x + 1) // List(2, 3, 4, 5)

val buff = mutable.ArrayBuffer(1, 2, 3, 4)
buff.map(x => x % 2 == 0) // mutable.ArrayBuffer(false, true, false, true)

val map = Map(0 -> "No", 1 -> "Yes")
map.map.((key, value) => key -> (value * 2)) // Map(0 -> NoNo, 1 -> YesYes)
```

> Note: adter calling `map` the number of elements is unchanged. For sequences the order is also unchanged.

We can also transform a collection with `flatMap`, which allows us to change the number of elements in the collection.

```scala
// Remove all the elements
List(1, 2, 3).flatMap(x => List())

// Double the number of elements
mutable.ArrayBuffer(1, 2).flatMap(x => mutable.ArrayBuffer(x, x * 2))

// Keep the number of elements the same but change type
Map(0 -> "zero", 1 -> "one").flatMap((key, _) =>
    Map(key.toString -> key)
)
```

So let's implement a program that return all the phone numbers of a contact list to check the differences between `map` and `flatMap`

```scala
case class Contact(name: String, phoneNumbers: List[String])
val contacts: List[Contact] = ...
```

First attempt

```scala
val allPhoneNumbers = contacts.map(contact => contact.phoneNumbers)
// allPhoneNumbers = List(List(), List("+41787828420"))
```

We get back a `List[List[String]]`, but ideally we would like to flatten the nested list at the top-level. We can do so with the `flatMap` operation:

```scala
val allPhoneNumbers = contacts.flatMap(contact => contact.phoneNumbers)
// allPhoneNumbers = List("+41787828420")
```

The `folfLeft` methods allow us to transform a collection into _anything_ else. Let's see some examples.

```scala
// Sum the elements of the list (in practice we would use the sum method)
List(1, 2, 3).foldLeft(0)((accumulator, element) => accumulator + element) // 6

// Reverse the list (in practice we would use the reverse method)
List(1, 2, 3).foldLeft(List.empty[Int])((accumulator, element) => 
    element +: accumulator
) // List(3, 2, 1)

// True if the last element is even or the list is empty
List(1, 2, 3).foldLeft(true)((accumulator, element) => element % 2 == 0)
```

The method `foldLeft` has two parameters lists.

Generally, methods can have more than one parameter list.

```scala
def foo(x: Int, y: Int)(f: Int => Int): Int
```

We call it as follow:

```scala
foo(0, 10)(i => i * i)
```

// understanding foldLeft
The understanding of `foldLeft` could be clearer from two ways. Let's start with this example:

```scala
// Sum the elements of the list (in practice we would use the sum method)
List(1, 2, 3).foldLeft(0)((accumulator, element) => accumulator + element) // 6
```

We can think of the first argument `0`, as an accumulator. The accumulator is the value we have computed over the portion of the list we have seen so far. The initial value of the accumulator is `0`, meaning the sum of the empty list is `0`.

The second argument, the function, is how we combine the accumulator with the current element of the list to create a new accumulator. In this example we add the element to the accumulator. Since the accumulator is the sum so far, the result will be the sum of all the elements.

Here is the second way of understanding `foldLeft`. We will se that `foldLeft` processes the list in the reverse order to which it is constructed, and we use the initial value of the accumulator at the start instedd of `Nil` at the end.

Let's start with

```scala
val data = List(1, 2, 3)
```

Remember that we can write this list as

```scala
val data = 1 :: 2 :: 3 :: `Nil`
```

List are constructed from right to left. Bracket make this explicit.

```scala
val data = 1 :: (2 :: (3 :: `Nil`))
```

`foldLeft` processes the list from left to right, which is opposite to the construction order. Let's define a function `add`.

```scala
val add = (x: Int, y: Int) => x + y
```

Then

```scala
(1 :: 2 :: 3 :: Nil).foldLeft(0)(add)
```

is equivalent to

```scala
add(add(add(0, 1), 2), 3)
```

Notice that `foldLeft` goes from left-to-right, and insted of the list finishing with `Nil`, `foldLeft` starts with the initial accumulator `0`.

We can extend this way of understanding `foldLeft` to the general case:

```scala
xs.foldLeft(z)(f) == f(f(f(f(z, xs(0)), xs(1)), ...), xs(n - 1))
```

In words it says we start at the left of the list, combining the initial element with the accumulator, and then proceed from left to right combining partial results with the next element we encounter.

The explanations above both used `List`. We can generalize to any collection by assuming (possibly arbitrary) ordering on the elements. For example, we could write

```scala
mutable.ArrayBuffer(1, 2, 3)
```

as

```scala
1 +: 2 +: 3+: mutable.ArrayBuffer.emty[Int]
```

and then the second explanation of `foldLeft` can be transferred to `ArrayBuffer`.

`groupBy` groups elements of a collection according to a partition function. For instance, to group email addresses by domains:

```scala
val emails = List("pam@sca.la", "jim@sca.la", "michael@the.off")

val domain: String => String =
    email => email.dropWhile(c => c != '@').drop(1)

val emailsByDomain = emails.groupBy(domain)
// emailsByDomain: Map[String, List[String]] = Map(
//    "sca.la" -> List("pam@sca.la", "jim@sca.la")
//    "the.off" -> List("michael@the.off")
//)
```

In summary, below are the methods for transforming collections:

- `map` for transforming elements while keeping the general structure of the collection;
- `flatMap` for transforming elements and possibly transforming some part of the structure of the collection; and
- `foldLeft` for transforming a collection in any way; and
- `groupBy` for grouping elements by a partition function.

## Sequences and Maps

In this section we will learn about two broad group of collections – sequences and maps – and features that are specific to each group.

Some collections are sequences, which means the collection's elements have a defined ordering, usually the order in whihc they are inserted into the collection.

`List` and `ArrayBuffer` are sequences. `Map` is not.

With a sequence we can get the first element of the sequence – the `head` – and the rest of the sequence – the `tail` –

```scala
val data = mutable.ArrayBuffer(1, 2, 3)
data.head //1
data.tail // ArrayBuffer(2, 3)
```

> Note: if the sequence is empty we will get an exception when we use `head` or `tail`.

List are _linear sequences_: accessing the nth element requires iterating through the first n - 1 elements. So, accessing the element at index n is an O(n) operation.

Array buffers are _indexed_ sequences: accessing an element at any index takes the same time and therefore is an O(1) operation.

Both data structures have pros an cons. It is your responsibility to choose the right data structure according to your needs! 

Use the method `sortBy` to sort sequences.

```scala
val data = List(
    "Alice" -> 42,
    "Bob" -> 30,
    "Werner" -> 77,
    "Owl" -> 6,
)

data.sortBy((_, age) => age)
// List[(String, Int)] = List((Owl, 6), (Bob, 30), (Alice, 42), (Werner, 77))

data.sortBy((name, _) => name)
// List[(String, Int)] = List((Alice, 42), (Bob, 30), (Owl, 6), (Werner, 77))
```

`get` is the most important mehtod on `Map` that we have not yet explorer. It allow us to get the element associated with a key.

```scala
val data = Map("a" -> 0, "b" -> 1, "c" -> 2)  
data.get("a") // Some(0)
data.get("z") // None
```
> Note: `get` return an `Option` as there may not be an element associated with the key.

We have learned that sequences are collections whose elements have a defined order. We saw that:

- we can access the first and remainder of the sequence with `head` and `tail`.
- we can sort a sequence using `sortBy`
- some sequences are linear, meaning the time to access an element is proportional to how far it is from the start, and some are indexed, meaning all elements take the same time to access.
- We also learned that maps have method `get` to access values by key.

## Option

The special case of collection that has at most one element is named `Option`.

Like the collections, the type `Option` is parameterized bythe type of element:

- `Option[Int]` is the type of an optional integer value
- `Option[Boolean]` is the type of an optional boolean value

An optional value can either be `None` (absence of value), or `Some(value)`.

Optional values have similar operations as collections (`map`, `filter`, `flatMap`, `foldLeft`, `isEmpty`, `contains`, etc.).

Let us revisit the contact data type and make the `email` field optional:

```scala
case class Contact(
    name: String,
    maybeEmail: Option[String],
    phoneNumbers: List[String]
)

val pam = Contact("Pam", Some("pam@sca.la"),  List())
val jim = Contact("Jim", None,  List("+417878829420"))
```

> Note: it is common in practice to prefix identifiers of optional value wiht "maybe" as in `maybeEmail`.

We can also pattern match on option as shown the next snippet:

```scala
def hasDotLaEmail(contact: Contact): Boolean =
    contact.maybeEmail match
        case Some(email) => email.endsWith("@sca.la")
        case None => false
```
The operation `map` transforms the element in the option with the given function. The operation `getOrElse` returns the optional value, if it is defined, or falls back to a given value.

```scala
def emailLength(contact: Contact) : Int =
    contact.maybeEmail
        .map(email => email.size)
        .getOrElese(0)
```

The operation `zip` combines two optional values into a single optional value containing a pair. The resulting value is defined only if the two original optional values were defined.

```scala
val maybePamAndJimEmails: Options[(String, String)] =
    pam.maybeEmail.zip(jim.maybeEmail)
```

We have already seen that the opration `find` on collections returns `None` in case no elments were found.

Another example is the operation `headOption` on `List` that optionally returns the first element of the list if the list is not empty.

```scala
def sendNotification(contact: Contact, message: String): Unit =
    contact.phoneNumbers.headOption match
        case Some(number) => sendSms(number, message)
        case None =>
            contact.maybeEmail match
                case Some(email) => sendEmail(email, message)
                case None => ()
```

In some programming languages, the value `null` (or equivalent) is used to model optional values. However, it is now largely admitted that this is bad practice:

- [Null References The Billion Dolar Mistake](https://www.infoq.com/presentations/Null-References-The-Billion-Dollar-Mistake-Tony-Hoare/)

In Scala, there is a `null` value, but is here mostly fo interoperability with Java. We recommend modeling optional values with the type `Option` instead.

To summarize, the type `Option` models optional values. A value of type `Option[A]` can either be `None`, or `Some(a: A)`. Some collections operations return optional values (e.g., `find`, `headOption`).

## Introducing Loops
Let's check different ways to write loops. Most algorithms involve some kind of loops.

For instance, sorting a sequence requires iterating on its elements until ther are sorted.

Finding the shortest path between two points in a graph requires iterating on the graph nodes and edges until the destination is reached.

In this section we will present several ways of performing loops:

- iterating on the standard collecitions,
- _imperative loops_ with the controls structure `while`, 
- _functional loops_ with recursion

Depending on the situation, some ways are more convenient than others.

Let's introduce the loop concept with the factorial of a positive integer where given a `n` the factorial is the product of all positive integers less than or equal o `n`:

```scala
def factorial(n: Int): Int =
    1 * 2 * ... * (n - 2) * (n - 1) * n 
```
We have already seen that the Scala collections have operations that iterate on all their elements. The most general one is `foldLeft`. we can implement `factorial` with `foldLeft` as follows:

```scala
def factorial(n: Int): Int =
    (1 to n).foldLeft(1)((result, x) => result * x)
```

First, we create a collection of numbers from 1 to n, then we compute the product of all these numbers.

> Note: There is also an operation `product`:

```scala
def factorial(n: Int): Int = (1 to n).product

```

`Range` is another type of Scala that models a range of values. Here are some self-explanatory examples:

```
1 to 4 // 1, 2, 3, 4
0 to 10 by 2 // 0, 2, 4, 6, 8, 10
5 until 0 by -1 // 5, 4, 3, 2, 1
```

The operations `to` and `until` are regular operations on type `Int`:

```
1.to(4) // : Range
```

Ranges have the opeartions of sequnces, such as `map`, `filter`, etc.

Another way of doing loops consists in using the `while` control flow statement:

```scala
def factorial(n: Int): Int =
    var acc = 1
    var i = 1

    while i < n do
        i = i + 1
        acc = acc + i
    acc
```

The keyword `var` introduces a variable definition. Unlike `val` definitions, `var` definitions can be **re-assigned** a new right-hand side over time.

The general syntax for the `while` control flow statement is:

```scala
while <condition> do <statement>

while <condition> { <statement> } // Scala 2 Syntax
```

Last, another way of doing loops is to use _recursive functions_. Recursive functions are functions defined in terms of themselves:

```scala
def factorial(n: Int): Int =
    if n == 0 then 1
    else n * factorial(n - 1)
```
Here, `factorial(n)` is defined in terms of `factorial(n - 1)`, which is defined in terms of `factorial(n - 2)`, and so on untl we reach `factorial(0)`, which terminates the loop.

Let's look at the evaluation steps of the expression `factoria(3)`:

```
- 3 * factorial(3 - 1)
- 3 * 2 * factorial(2 - 1)
- 3 * 2 * 1 * factorial(1 - 1)
- 3 * 2 * 1 * 1
- 6
```

`List` is a recursive type: a `tail` is itself a `List`.

This pattern is **structural recursion**. Generally, recursive data types area esasy to process with strcuturally recursive algorithms.

In summary, looping can be achieved with several approaches:

- iterating on collections,
- `while` control-flow statements,
- recursive functions.

Several styles of loops are possible.

Often exploring the solution space of a problem is easy to express with the existing collections operations.

Generally, recursion works well with the recursive data types.

According to the situation, pick the solutions that works better for you and your team.

## Tail Recusrion

Let's remind the factirial function defined before:

```scala
def factorial(n: Int): Int =
    if n == 0 then 1
    else n * factorial(n - 1)
```

and below is the evaluation of `factorial(3)`

```
- 3 * factorial(3 - 1)
- 3 * 2 * factorial(2 - 1)
- 3 * 2 * 1 * factorial(1 - 1)
- 3 * 2 * 1 * 1
- 6
```

Each time the runtime evaluates a `factorial` call, it pushes its parameter to the **call stack**.

If the chain of recursive calls is to long the call stack overflows, which produces a runtime error called `StackOverflowError`.

The maximal number of iterations before getting a `StackOverflowError` depends on the runtime, it is in general around tens of thousands.

Luckily, it is possible to not use stack space by putting the recursive call in _tail_ position.

A recursive call is in tail position if it is the result of the recursive method (i.e., there is no futher operation applied to it).

```scala
def factorial(n: Int): Int = 
    def factorialTailRec(x: Int, accumulator: Int): Int =
        if x == 0 then accumulator
        else factorialTailRec(x - 1, x * accumulator)
    
    factorialTailRec(n, 1)
end facotrial
```

With this definition, call to `factorialTailRec` do not need to use space on the call stack. Below the evaluation of this version:

```
- factorialTailRec(3 - 1)
- factorialTailRec(3 - 1, 3 * 1)
- factorialTailRec(2, 3)
- factorialTailRec(2 - 1, 2 * 3)
- factorialTailRec(1, 6)
- factorialTailRec(1 - 1, 1 * 6)
- factorialTailRec(0, 6)
- 6
```

In summary, the call stack size may limit the number of possible iterations of a recrusive methods, unless the recursive call is in tail position.

## "for" Syntax

Iterating over collections or transforming collections is so common in programming that Scala supports a special syntax aiming at making such pieces of code more readable.

As an example, consider a program that computes a list of phone numbers that start with the country prefix "+41", and the name of the contact belong to:

```scala
val namesAndSwissNumbers: List[(String, String)] =
    contacts.flatMap {
        contact => contact.phoneNumbers
            .filter(phoneNumber => phoneNumber.startsWith("+41"))
            .map(phoneNumber => (contact.name, phoneNumber))
    }
```

The same program can be expressed as a "for expression":

```scala
val namesAndSwissNumbers: List[(String, String)] =
    for
        contact <- contacts
        phoneNumber <- contact.phoneNumbers
        if phoneNumber.startsWith("+41")
    yield (contact.name, phoneNumber)
```

A for-expression is of the form

```
for ( s ) yield e
```

where `s` is a sequence of _generators_ and _filters_, and `e` is an expression whose value is returned by an iteration.

- A _generator_ is of the form `p <- e`, where `p` is a pattern and `e` an expression whose value is a collection.
- A _filter_ is of the form `if f` where `f` is a boolean expression.
- The sequence must start with a generator.
- If there are several generators in the sequence, the last generators vary faster than the first.

Instead of `( s )`, the sequence of generators and filters can be written on multiplen lines without requiring semicolons.

The Scala compiler translates for-expressions in terms of `map`, `flatMap` and a lazy variant of `filter`. Here is the translations scheme used by the compiler.

1. A simple for-expression

```
for x <- e1 yield e2
```

is translated to

```
e1.map(x => e2)
```

2. A for-expression

```
for (x <- e1 if f; s) yield e2
```

where `f` is a filter and `s` is a (potentially empty) sequence of generators and filters is translated to

```
for (x <- e1.withFilter(x => f); s) yield e2
// and the translations continues with the new expression
```

You can think of `withFilter` as a variant of `filter` that does not produce an itermediate list, but instead filters the following `map` or `flatMap` function application.

3. A for expression

```
for(x <- e1; y <- e2; s) yield e3
```

where `s` is a sequence of generators and filters, is translated to

```
e1.flatMap(x => for (y <- e2; s) yield e3)
// and the translation continues with the new expression
```

Imperative loops also have a special syntax. The statement:

```
for x <- e1 do s
// Note the usage of `do` instead of `yield`)
```

Is translated to:

```
e1.foreach(x => s)
```

The `foreach` does not return a new collection. It is useful to perform side effects like printing the elements of a collection.

In summary `for` expressions and statements _desugar_ to calls to collections operations.

They can make combinatorial search more readable.

// ✅  🚧  🛑