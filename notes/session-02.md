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