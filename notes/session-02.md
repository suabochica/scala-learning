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

// TODO:

We started our introduction to collection by looking at three examples of collections types: `List`, `ArrayBuffer`, and `Map` and learned about the packages in which they are found.