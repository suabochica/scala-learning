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