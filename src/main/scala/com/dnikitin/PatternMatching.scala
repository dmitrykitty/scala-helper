package com.dnikitin
package com.dnikitin

@main def main(): Unit = {

  /*
    Pattern matching is like switch, but much more powerful.

    Important:
      - match is an expression, so it returns a value
      - cases are checked from top to bottom
      - _ means "anything else"
   */

  val anInteger = 55

  val order = anInteger match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => anInteger + "th"
  }

  println(order)


  /*
    Guards

    We can add conditions to cases with `if`.
   */

  val numberDescription = anInteger match {
    case 1 => "one"
    case 2 => "two"
    case n if n % 2 == 0 => "even number"
    case n if n % 2 != 0 => "odd number"
    case _ => "unknown"
  }

  println(numberDescription)


  /*
    Case classes work very well with pattern matching.

    case class automatically creates:
      - constructor
      - apply method
      - unapply method
      - toString
      - equals
      - copy
   */

  case class Person(name: String, age: Int)

  val bob = Person("Bob", 18) // Person.apply("Bob", 18)

  /*
    Pattern matching can deconstruct case class instances.
    Person(n, a) extracts name and age from bob.
   */

  val personGreeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I am $a years old"
  }

  println(personGreeting)


  /*
    Pattern matching with guards on case classes.
   */

  val personDescription = bob match {
    case Person(name, age) if age < 18 =>
      s"$name is underage"

    case Person(name, age) if age >= 18 =>
      s"$name is adult"

    case _ =>
      "Unknown person"
  }

  println(personDescription)


  /*
    Constants in pattern matching.
   */

  val specialName = "Bob"

  val specialGreeting = bob match {
    case Person(`specialName`, age) =>
      s"This is special person Bob, age = $age"

    case Person(name, age) =>
      s"Normal person: $name, age = $age"
  }

  println(specialGreeting)

  /*
    Important:
    `specialName` means compare with existing variable.

    Without backticks:

      case Person(specialName, age)

    Scala would create a NEW variable called specialName.
   */


  /*
    Tuple decomposition
   */

  val aTuple = ("Bon Jovi", "Rock")

  val bandDescription = aTuple match {
    case (artist, style) =>
      s"Artist: $artist; Music style: $style"
  }

  println(bandDescription)


  /*
    Nested tuple pattern
   */

  val nestedTuple = ("Metallica", ("Metal", 1981))

  val nestedTupleDescription = nestedTuple match {
    case (band, (genre, year)) =>
      s"$band plays $genre and started in $year"
  }

  println(nestedTupleDescription)


  /*
    List decomposition - exact structure.
   */

  val aList = List(1, 2, 3)

  val listStructure = aList match {
    case List(_, 2, _) =>
      "List with exactly 3 elements and 2 in the middle"

    case _ =>
      "Something else"
  }

  println(listStructure)


  /*
    List decomposition with ::

    :: means "head and tail".

    head :: tail means:
      - head = first element
      - tail = the rest of the list
   */

  val listDescription = aList match {
    case Nil =>
      "Empty list"

    case head :: Nil =>
      s"List with one element: $head"

    case head :: second :: Nil =>
      s"List with two elements: $head and $second"

    case head :: tail =>
      s"List starts with $head, rest is $tail"
  }

  println(listDescription)


  /*
    Examples:

    List(1, 2, 3) match {
      case head :: tail
    }

    head = 1
    tail = List(2, 3)

    List(1) match {
      case head :: Nil
    }

    head = 1
    Nil = empty list
   */


  /*
    Type patterns
   */

  val something: Any = "Scala"

  val typeDescription = something match {
    case s: String =>
      s"This is a String with length ${s.length}"

    case i: Int =>
      s"This is an Int: $i"

    case b: Boolean =>
      s"This is a Boolean: $b"

    case _ =>
      "Unknown type"
  }

  println(typeDescription)


  /*
    Alternatives with |
   */

  val day = "Saturday"

  val dayType = day match {
    case "Saturday" | "Sunday" =>
      "weekend"

    case "Monday" | "Tuesday" | "Wednesday" | "Thursday" | "Friday" =>
      "working day"

    case _ =>
      "unknown day"
  }

  println(dayType)


  /*
    Pattern matching can be used in methods.
   */

  def describeList(list: List[Int]): String = {
    list match {
      case Nil =>
        "empty list"

      case head :: Nil =>
        s"one element: $head"

      case head :: tail =>
        s"head = $head, tail = $tail"
    }
  }

  println(describeList(List()))
  println(describeList(List(10)))
  println(describeList(List(10, 20, 30)))
}