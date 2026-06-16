package com.dnikitin

@main def functionalMain(): Unit = {

  /*
    apply method
    If a class/object has apply(...), we can call it like a function.
   */
  class Person(name: String) {
    def apply(age: Int): Unit = {
      println(s"My name is $name and I am $age years old")
    }

    def greet(): Unit = {
      println(s"Hi, I am $name")
    }
  }

  val bob: Person = new Person("Bob")

  bob.greet()
  bob.apply(24)
  bob(24) // Same as bob.apply(24)


  /*
    Scala runs on the JVM.

    Functional programming features:
      - compose functions
      - pass functions as arguments
      - return functions as results

    On the JVM functions are represented as objects.
    Scala has FunctionX traits:
      Function1, Function2, ..., Function22

    Example:
      Function1[Int, Int] means:
      function takes Int and returns Int
   */

  val simpleIncrementer = new Function1[Int, Int] {
    override def apply(arg: Int): Int = arg + 1
  }

  println(simpleIncrementer.apply(12))
  println(simpleIncrementer(12)) // Same as apply

  // All Scala functions are instances of FunctionX types.

  val stringConcatenator = new Function2[String, String, String] {
    override def apply(v1: String, v2: String): String = v1 + v2
  }

  println(stringConcatenator("A", "B"))


  /*
    Function syntax
   */

  // Full type
  val doubler: Function1[Int, Int] = (x: Int) => 2 * x

  // Shorter type
  val doubler2: Int => Int = (num: Int) => 2 * num

  // Even shorter, type is inferred
  val tripler = (x: Int) => x * 3

  println(doubler(2))  // 4
  println(doubler2(3)) // 6
  println(tripler(4))  // 12


  /*
    Function composition
   */

  val plusOne: Int => Int = x => x + 1
  val timesTwo: Int => Int = x => x * 2

  val plusOneThenTimesTwo = plusOne.andThen(timesTwo)
  val timesTwoThenPlusOne = plusOne.compose(timesTwo)

  println(plusOneThenTimesTwo(10)) // (10 + 1) * 2 = 22
  println(timesTwoThenPlusOne(10)) // (10 * 2) + 1 = 21


  /*
    Higher-order functions

    Higher-order function is a function which:
      - takes another function as argument
      - or returns another function
   */

  def applyFunction(x: Int, f: Int => Int): Int = {
    f(x)
  }

  println(applyFunction(10, plusOne))     // 11
  println(applyFunction(10, x => x * 10)) // 100

  def multiplier(factor: Int): Int => Int = {
    x => x * factor
  }

  val multiplyBy5 = multiplier(5)
  println(multiplyBy5(10)) // 50


  /*
    map, flatMap, filter
   */

  val aMappedList: List[Int] =
    List(1, 2, 3).map(x => x + 1)

  println(aMappedList) // List(2, 3, 4)

  // flatMap expects a function which returns a collection
  val aFlatMappedList: List[Int] =
    List(1, 2, 3, 4).flatMap(x => List(x, x * x))

  println(aFlatMappedList)
  // List(1, 1, 2, 4, 3, 9, 4, 16)

  // Alternative syntax for longer lambdas
  val aFlatMappedList2 = List(1, 2, 3, 4).flatMap { x =>
    List(x, x * x)
  }

  /*
    Your previous version:

    List(1, 2, 3, 4).flatMap(x => List(for (n <- 1 to x) yield n * n))

    Problem:
    for (...) yield ... already creates a collection.
    Then you wrapped it into List(...), so result becomes nested.
   */

  val squaresFromRanges: List[Int] =
    List(1, 2, 3, 4).flatMap { x =>
      (1 to x).map(n => n * n)
    }

  println(squaresFromRanges)
  // List(1, 1, 4, 1, 4, 9, 1, 4, 9, 16)


  /*
    filter
   */

  val greaterThan3 =
    List(1, 5, 2, 7, -1, 4, 9).filter(x => x > 3)

  val greaterThan5 =
    List(1, 2, 3, 4, 5, 6, 7).filter(_ > 5)

  println(greaterThan3) // List(5, 7, 4, 9)
  println(greaterThan5) // List(6, 7)


  /*
    Chaining
   */

  val processedList =
    List(1, 2, 3, 4, 5)
      .filter(_ % 2 == 1)
      .map(_ * 10)

  println(processedList) // List(10, 30, 50)


  /*
    Create all possible pairs:
    1, 2, 3
    a, b, c
   */

  val allPairs =
    List(1, 2, 3).flatMap(num =>
      List('a', 'b', 'c').map(ch => s"$num-$ch")
    )

  println(allPairs)


  /*
    For comprehensions

    Compiler rewrites this to map / flatMap / filter.
   */

  val alternativePairs = for {
    number <- List(1, 2, 3)
    letter <- List('a', 'b', 'c')
  } yield s"$number-$letter"

  println(alternativePairs)

  val onlyEvenPairs = for {
    number <- List(1, 2, 3, 4)
    if number % 2 == 0
    letter <- List('a', 'b')
  } yield s"$number-$letter"

  println(onlyEvenPairs)


  /*
    Collections
   */

  // List - immutable linked list
  val aList = List(1, 2, 3, 4)

  val first = aList.head // 1
  val rest = aList.tail  // List(2, 3, 4)

  val aPrependedList = 0 :: aList     // List(0, 1, 2, 3, 4)
  val anotherPrepended = 0 +: aList   // List(0, 1, 2, 3, 4)
  val aAppendedList = aList :+ 5      // List(1, 2, 3, 4, 5)

  println(aList.reverse)
  println(aList.contains(3))
  println(aList.sum)


  // Seq - general sequence
  val aSeq: Seq[Int] = Seq(1, 2, 3)
  val accessedElement = aSeq(2) // 3

  println(accessedElement)


  // Vector - good default immutable sequence for larger data
  val aVector = Vector(1, 2, 3, 4)
  val updatedVector = aVector.updated(0, 100)

  println(updatedVector) // Vector(100, 2, 3, 4)


  // Sets - no duplicates
  val aSet = Set(1, 2, 3, 1, 2)

  val setHas5 = aSet.contains(5) // false
  val addedToSet = aSet + 5
  val removedFromSet = aSet - 1

  println(aSet)          // Set(1, 2, 3)
  println(addedToSet)    // Set(1, 2, 3, 5)
  println(removedFromSet)


  // Ranges
  val aRange = 1 to 10
  val exclusiveRange = 1 until 10

  val twoByTwo = aRange.map(_ * 2).toList

  println(twoByTwo)


  // Tuples - group values together
  val aTuple = ("Bon Jovi", "Rock", 1982)

  val bandName = aTuple._1
  val genre = aTuple._2
  val year = aTuple._3

  val (name, musicGenre, startYear) = aTuple

  println(s"$name, $musicGenre, $startYear")


  // Map - key/value collection
  val aPhoneBook: Map[String, String] = Map(
    ("j1", "354646"),
    ("j2", "5346356"),
    "j3" -> "36363"
  )

  val number1 = aPhoneBook("j1") // unsafe if key does not exist
  val numberOption = aPhoneBook.get("j4") // safe, returns Option[String]

  println(number1)
  println(numberOption) // None

  val updatedPhoneBook = aPhoneBook + ("j4" -> "99999")
  val removedFromPhoneBook = aPhoneBook - "j1"

  println(updatedPhoneBook)
  println(removedFromPhoneBook)


  /*
    Option

    Option is used when value can be missing.
    It can be:
      Some(value)
      None
   */

  val maybeNumber: Option[String] = aPhoneBook.get("j2")

  val result = maybeNumber match {
    case Some(number) => s"Found number: $number"
    case None         => "Number not found"
  }

  println(result)

  val numberOrDefault = aPhoneBook.getOrElse("unknown", "00000")
  println(numberOrDefault)
}